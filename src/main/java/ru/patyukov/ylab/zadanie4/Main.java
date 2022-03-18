package ru.patyukov.ylab.zadanie4;

import org.xml.sax.SAXException;
import ru.patyukov.ylab.zadanie4.xml.DomParser;
import ru.patyukov.ylab.zadanie4.xml.GameResult;
import ru.patyukov.ylab.zadanie4.xml.Gameplay;
import ru.patyukov.ylab.zadanie4.xo.Cell;
import ru.patyukov.ylab.zadanie4.xo.Field;
import ru.patyukov.ylab.zadanie4.xo.Player;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

// Класс управления игры.
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Field field = new Field(); // Создаем поле.
    private static Gameplay gameplayWrite;   // Объект который хранит историю игры.
    private static Player player1;          // Первый игрок.
    private static Player player2;         // Второй игрок.
    public static String path;            // Файл который хранит историю игры.
    private static int i;               // Количество ходов

    public static void main(String[] args) {

        System.out.println("\n\n\t\tКРЕСТИКИ НОЛИКИ\n");

        gameResult();   // Метод просмотра истории.
        System.out.println("=========================================================\n\n");
        createPlayer();  // Создаем игроков.
        queue();        // Определяем кто первым начнет.
        path = nameFileXML(player1.getName(), player2.getName());   // Создаем файл который хранит историю игры, если его нет.

            // ИГРА НАЧАЛАСЬ.
        for (i = 1; i > 0; i++) {    // Условие i > 0 написано с осознанием полной ответственности за результат работы бессконечного цикла !!!
                                    // i - количество ходов. На последнем ходе будет break.
            field.printFiled();    // Выводим поле.

                // ПОДВОДИМ ИТОГИ ОЧЕРЕДНОГО ХОДА.

            String namePlayer = field.gameOverFinish();   // Получаем имя победителя, если такой есть. Иначе пустую строку.

            // Обрабатываем победителя, если он есть.
            if (!namePlayer.equals("")) {
                finish(namePlayer);
                break;
            }

            // Проверяем на ничью.
            if (!field.gameOver()) {
                draw();   // Обрабатываем ничью.
                break;
            }

            if (player1.isStartStop()) goPlayer1();         // Первый игрок делат очередной ход.
            else if (player2.isStartStop()) goPlayer2();   // Второй игрок делат очередной ход.
        }

        // Метод просмотра истории.
        gameResult();
    }

    // Первый игрок делат очередной ход.
    public static void goPlayer1() {
        Cell cell = Cell.xy(player1, field.getN());
        if (cell != null) {
            if (field.saveCellInField(cell, player1)) {
                gameplayWrite.setGame(player1.getId(), String.valueOf(i), cell.getX(), cell.getY());   // Добавляем очередной ход.
                player1.setStartStop(false);
                player2.setStartStop(true);
            }
        }
        else {
            System.out.println("Входные данные метода public static Cell xy(Player player, int n)");
            System.out.println("Не соответствуют трнебованиям");
            return;
        }
    }

    // Второй игрок делат очередной ход.
    public static void goPlayer2() {
        Cell cell = Cell.xy(player2, field.getN());
        if (cell != null) {
            if (field.saveCellInField(cell, player2)) {
                gameplayWrite.setGame(player2.getId(), String.valueOf(i), cell.getX(), cell.getY());   // Добавляем очередной ход.
                player1.setStartStop(true);
                player2.setStartStop(false);
            }
        }
        else {
            System.out.println("Входные данные метода public static Cell xy(Player player, int n)");
            System.out.println("Не соответствуют трнебованиям");
            return;
        }
    }

    // Метод обрабатывет победителя.
    public static void finish(String namePlayer) {
        System.out.println("\n" + namePlayer + " - ВЫИГРАЛ !!!");
        System.out.println("=========================================================\n\n");

        // Сохраняем победителя в объект который хранит историю игры.
        if (player1.getName().equals(namePlayer)) gameplayWrite.setGameResult(new GameResult(player1));
        else gameplayWrite.setGameResult(new GameResult(player2));

        // Сохраняем объект который хранит историю игры в файл xml.
        try {
            DomParser.write(gameplayWrite, path);
        } catch (ParserConfigurationException e) {
            System.out.println("Не удалось сохранить историю игры");
            e.printStackTrace();
        } catch (TransformerException e) {
            System.out.println("Не удалось сохранить историю игры");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось сохранить историю игры");
            e.printStackTrace();
        }

        // Сохраняем статистику.
        if (namePlayer.equals(player1.getName())) Statisticsplayer.statisticsPlayer(player1.getName(), player2.getName());
        else Statisticsplayer.statisticsPlayer(player2.getName(), player1.getName());
    }

    // Метод обрабатывает ничью.
    public static void draw() {
        System.out.println("\nНИЧЬЯ !!!");
        System.out.println("=========================================================\n\n");

        gameplayWrite.setGameResult(null);   // Сохраняем null в объект который хранит историю игры.

        // Сохраняем объект который хранит историю игры в файл xml.
        try {
            DomParser.write(gameplayWrite, path);
        } catch (ParserConfigurationException e) {
            System.out.println("Не удалось сохранить историю игры");
            e.printStackTrace();
        } catch (TransformerException e) {
            System.out.println("Не удалось сохранить историю игры");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось сохранить историю игры");
            e.printStackTrace();
        }
    }

    // Определяем кто первым начнет.
    public static void queue() {

        /*
            Кто первый начнет у того id = "1".
            Кто второй начнет у того id = "2".
         */

        if ( ((int) ((Math.random()) * 10)) < 5 ) {
            player1.setStartStop(true);   // Начьнет первый.
            player1.setId("1");   // Задаем id первого игрока.
            player2.setId("2");   // Задаем id второго игрока.
            gameplayWrite = new Gameplay(player1, player2);   // Инициализируем объект который хранит историю игры.
        }
        else {
            player2.setStartStop(true);   // Начьнет второй.
            player2.setId("1");   // Задаем id второго игрока.
            player1.setId("2");   // Задаем id первого игрока.
            gameplayWrite = new Gameplay(player2, player1);   // Инициализируем объект который хранит историю игры.
        }
    }

    // Создаем игроков.
    public static void createPlayer() {

        // Первый игрок.
        System.out.print("Введите имя первого игрока - ");
        String namePlayer = scanner.nextLine();
        while (true) {
            System.out.print("Введите символ первого игрока (Х или О) - ");
            String namePlayerValue = scanner.nextLine();
            if (namePlayerValue.equals("Х") || namePlayerValue.equals("О")) {
                player1 = new Player(namePlayer, namePlayerValue);   // Создаем первого игрока.
                break;
            }
        }

        // Второй игрок.
        System.out.print("\nВведите имя второго игрока - ");
        namePlayer = scanner.nextLine();
        if (player1.getValue().equals("О")) player2 = new Player(namePlayer, "Х");   // Создаем первого игрока.
        else player2 = new Player(namePlayer, "О");   // Создаем первого игрока.
    }

    // Метод просмотра истории.
    public static void gameResult() {

        Scanner scanner = new Scanner(System.in);
        ArrayList<String> strListPath = new ArrayList<>();   // Список имен файлов с историей игр, без директории и расширения.

        // Получаем список всех файлов из директории в которой хранится файлы с историей игр.
        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(Path.of("src/main/resources/static/file/zadanie4"));
            for (Path paht : paths) {   // Получаем список всех файлов.
                String strPaht = paht.getFileName().toString();   // Получаем имена файлов без директории.
                // Сохраняем только файлы с расширение xml, но расширение несохраняем.
                if (strPaht.endsWith(".xml")) strListPath.add(strPaht.substring(0, strPaht.length() - 4));
            }
        } catch (IOException e) {
            System.out.println("Не удалось посмотреть историю игр предыдущих игроков");
            System.out.println("Ошибка при загрузке списка имен файлов с историей игры.");
            e.printStackTrace();
            return;
        }

        // Цикл управления вывода истории.
        for (int j = 1; j > 0; j++) {    // Условие j > 0 написано с осознанием полной ответственности за результат работы бессконечного цикла !!!

            System.out.println("\tИстория игр предыдущих игроков:\n");

            // Выводим список имен файлов с историей игр.
            for (String buf : strListPath) System.out.println(buf);
            System.out.println();

            System.out.println("\tДля просмотра истории игры введите имя из списка");
            System.out.println("\tДля просмотра статистики игры введите STAT");
            System.out.println("\tДля продолжения введите NEXT\n");
            System.out.print("Введите - ");

            String flag = scanner.nextLine();   // Ввод от пользователя.

            // Обработка ввода.
            if (flag.equals("NEXT")) return;
            else if (flag.equals("STAT")) {
                System.out.println();
                Statisticsplayer.printStatisticsPlayer();
                System.out.println("*********************************************************");
                System.out.println("=========================================================\n\n");
            }
            else {
                System.out.println();

                // Ищем введенное имя в списке имен файлов с историей игр, без директории и расширения.
                for (int i = 0; i < strListPath.size(); i++) {
                    if (flag.equals(strListPath.get(i))) {
                        try {
                            System.out.println("\tИстория игры - " + flag);
                            Gameplay gameplay = DomParser.read("src/main/resources/static/file/zadanie4/" + flag + ".xml");
                            gameplay.printGameplay();
                        } catch (ParserConfigurationException e) {
                            System.out.println("Не удалось посмотреть историю игры предыдущих игроков");
                            System.out.println("Ошибка при загрузке файла с историей игры.");
                            e.printStackTrace();
                        } catch (IOException e) {
                            System.out.println("Не удалось посмотреть историю игры предыдущих игроков");
                            System.out.println("Ошибка при загрузке файла с историей игры.");
                            e.printStackTrace();
                        } catch (SAXException e) {
                            System.out.println("Не удалось посмотреть историю игры предыдущих игроков");
                            System.out.println("Ошибка при загрузке файла с историей игры.");
                            e.printStackTrace();
                        }
                        break;
                    }
                    if (i == strListPath.size() - 1) {
                        System.out.println("\tВведенное имя не найдено. ПОВТОРИТЕ !!!");
                        System.out.println("=========================================================\n\n");
                    }
                }
            }
        }
    }

    // Метод определяет имя файла для хранения истории.
    public static String nameFileXML(String namePlayer1, String namePlayer2) {

        String namePlayer1_and_namePlayer2 = "src/main/resources/static/file/zadanie4/" + namePlayer1 + "_and_" + namePlayer2 + ".xml";
        String namePlayer2_and_namePlayer1 = "src/main/resources/static/file/zadanie4/" + namePlayer2 + "_and_" + namePlayer1 + ".xml";

        if (!Files.exists(Path.of(namePlayer1_and_namePlayer2))) {
            if (Files.exists(Path.of(namePlayer2_and_namePlayer1))) return namePlayer2_and_namePlayer1;
            else return namePlayer1_and_namePlayer2;
        }
        else return namePlayer1_and_namePlayer2;

    }
}