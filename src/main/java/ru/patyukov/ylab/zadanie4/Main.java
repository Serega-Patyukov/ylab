package ru.patyukov.ylab.zadanie4;

import ru.patyukov.ylab.zadanie4.json.JsonSimpleParser;
import ru.patyukov.ylab.zadanie4.xml.DomParser;
import ru.patyukov.ylab.zadanie4.model.GameResult;
import ru.patyukov.ylab.zadanie4.model.Gameplay;
import ru.patyukov.ylab.zadanie4.xo.Cell;
import ru.patyukov.ylab.zadanie4.xo.Field;
import ru.patyukov.ylab.zadanie4.xo.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

// Класс управления игры.
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Field field;               // Поле.
    private static Gameplay gameplay;        // Объект который хранит историю игры.
    private static Player player1;          // Первый игрок.
    private static Player player2;         // Второй игрок.
    private static int i;                 // Количество ходов

    private static JsonSimpleParser jsonSimpleParser = new JsonSimpleParser();   // Обект класса который сохраняет и читает файл json.
    private static DomParser domParser = new DomParser();                       // Обект класса который сохраняет и читает файл xml.
    private static InterfaceParser parser;                                     // Переменная которая может парсить json и xml.

    public static String pathXML;         // Файл который хранит историю игры в xml.
    public static String pathJSON;       // Файл который хранит историю игры в json.

    private static boolean flag = true;   // Флаг завершения работы приложения.

    public static void main(String[] args) {

        while (flag) {
            System.out.println("\n\n\t\tКРЕСТИКИ НОЛИКИ\n");

            gameResult();   // Метод просмотра истории.
            if (!flag) break;

            field = new Field();   // Создаем поле.

            System.out.println("=========================================================\n\n");
            createPlayer();  // Создаем игроков.
            queue();        // Определяем кто первым начнет.

            pathJSON = nameFile(player1.getName(), player2.getName(), jsonSimpleParser);   // Создаем имя файла json который хранит историю игры.
            pathXML = nameFile(player1.getName(), player2.getName(), domParser);          // Создаем имя файлф xml который хранит историю игры.

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
        }
        System.out.println("\nКОНЕЦ !!!");
    }

    // Первый игрок делат очередной ход.
    public static void goPlayer1() {
        Cell cell = Cell.xy(player1, field.getN());
        if (cell != null) {
            if (field.saveCellInField(cell, player1)) {
                gameplay.setGame(player1.getId(), String.valueOf(i), cell.getX(), cell.getY());   // Добавляем очередной ход.
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
                gameplay.setGame(player2.getId(), String.valueOf(i), cell.getX(), cell.getY());   // Добавляем очередной ход.
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
        if (player1.getName().equals(namePlayer)) gameplay.setGameResult(new GameResult(player1));
        else gameplay.setGameResult(new GameResult(player2));

        // Сохраняем объект который хранит историю игры в файл xml.
        try {
            parser = domParser;
            parser.write(gameplay, pathXML);

            parser = jsonSimpleParser;
            parser.write(gameplay, pathJSON);
        } catch (Exception e) {
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

        GameResult gameResult = new GameResult();
        gameResult.setPlayer(null);

        gameplay.setGameResult(gameResult);   // Сохраняем null в объект который хранит историю игры.

        // Сохраняем объект который хранит историю игры в файл xml.
        try {
            parser = domParser;
            parser.write(gameplay, pathXML);

            parser = jsonSimpleParser;
            parser.write(gameplay, pathJSON);
        } catch (Exception e) {
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
            gameplay = new Gameplay(player1, player2);   // Инициализируем объект который хранит историю игры.
        }
        else {
            player2.setStartStop(true);   // Начьнет второй.
            player2.setId("1");   // Задаем id второго игрока.
            player1.setId("2");   // Задаем id первого игрока.
            gameplay = new Gameplay(player2, player1);   // Инициализируем объект который хранит историю игры.
        }
    }

    // Создаем игроков.
    public static void createPlayer() {

        int lengthNamePlayer = 3;   // Длина имени игрока.

        // Первый игрок.
        while (true) {
            System.out.println("\tВведите имя первого игрока");
            System.out.println("минимум 3 симврла\n");
            System.out.print("Имя - ");
            String namePlayer = scanner.nextLine();
            if (namePlayer.length() < lengthNamePlayer) {
                System.out.println("\nОШИБКА - неправельный ввод\n");
                continue;
            }
            while (true) {
                System.out.println("\n\tВведите символ первого игрока (Х или О)\n");
                System.out.print("Символ - ");
                String namePlayerValue = scanner.nextLine();
                if (namePlayerValue.equals("Х") || namePlayerValue.equals("О")) {
                    player1 = new Player(namePlayer, namePlayerValue);   // Создаем первого игрока.
                    break;
                }
                System.out.println("\nОШИБКА - неправельный ввод");
            }
            break;
        }

        // Второй игрок.
        while (true) {
            System.out.println("\n\tВведите имя второго игрока");
            System.out.println("минимум 3 симврла\n");
            System.out.print("Имя - ");
            String namePlayer = scanner.nextLine();
            if (namePlayer.length() < lengthNamePlayer) {
                System.out.println("\nОШИБКА - неправельный ввод");
                continue;
            }
            if (player1.getValue().equals("О")) player2 = new Player(namePlayer, "Х");   // Создаем первого игрока.
            else player2 = new Player(namePlayer, "О");   // Создаем первого игрока.
            break;
        }

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
                // Сохраняем только файлы с расширение xml и json.
                if (strPaht.endsWith(".xml")) strListPath.add(strPaht);
                if (strPaht.endsWith(".json")) strListPath.add(strPaht);
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

            System.out.println("\tДЛЯ ЗАПУСКА ИГРЫ ВВЕДИТЕ NEXT\n");
            System.out.println("\tДля просмотра истории игры введите имя из списка");
            System.out.println("\tДля просмотра статистики игры введите STAT");
            System.out.println("\tДля выхода введите EXIT\n");
            System.out.print("Введите - ");

            String buffer = scanner.nextLine();   // Ввод от пользователя.

            // Обработка ввода.
            if (buffer.equals("EXIT")) {
                flag = false;
                return;
            }
            else if (buffer.equals("STAT")) {
                System.out.println();
                Statisticsplayer.printStatisticsPlayer();
                System.out.println("*********************************************************");
                System.out.println("=========================================================\n\n");
            } else if (buffer.equals("NEXT"))  return;
            else {
                System.out.println();

                // Ищем введенное имя в списке имен файлов с историей игр, без директории и расширения.
                for (int i = 0; i < strListPath.size(); i++) {
                    if (buffer.equals(strListPath.get(i))) {
                        try {
                            System.out.println("\tИстория игры - " + buffer);

                            if (buffer.endsWith(".xml")) {
                                System.out.println("\nИстория игры из файла xml");
                                parser = domParser;
                                gameplay = parser.read("src/main/resources/static/file/zadanie4/" + buffer, null);
                                gameplay.printGameplay();
                            }

                            if (buffer.endsWith(".json")) {
                                System.out.println("\nИстория игры из файла json");
                                parser = jsonSimpleParser;
                                gameplay = parser.read("src/main/resources/static/file/zadanie4/" + buffer, null);
                                gameplay.printGameplay();
                            }

                        } catch (Exception e) {
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
    public static String nameFile(String namePlayer1, String namePlayer2, InterfaceParser parser) {

        String pathResult = "src/main/resources/static/file/zadanie4/" + namePlayer1 + "_and_" + namePlayer2;   // Оносительное имя файла.

        while (true) {

            // Для файлов .xml
            if (parser instanceof DomParser) {
                if (!Files.exists(Path.of(pathResult))) {
                    pathResult =  pathResult + "_0" + ".xml";
                    if (!Files.exists(Path.of(pathResult))) {
                        try {
                            FileWriter fileWriter = new FileWriter(pathResult);   // Создали пустой файл.
                        } catch (IOException e) {
                            System.out.println("Не удалось создать пустой файлл");
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                else {
                    String buf = String.valueOf(pathResult.charAt(pathResult.length() - 5));
                    int temp = Integer.parseInt(buf) + 1;
                    pathResult =  pathResult.substring(0, pathResult.length()-5) + temp + ".xml";
                    if (!Files.exists(Path.of(pathResult))) break;
                }
            }

            // Для файлов .json
            if (parser instanceof JsonSimpleParser) {
                if (!Files.exists(Path.of(pathResult))) {
                    pathResult =  pathResult + "_0" + ".json";
                    if (!Files.exists(Path.of(pathResult))) {
                        try {
                            Files.createFile(Path.of(pathResult));   // Создали пустой файл.
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                else {
                    String buf = String.valueOf(pathResult.charAt(pathResult.length() - 6));
                    int temp = Integer.parseInt(buf) + 1;
                    pathResult =  pathResult.substring(0, pathResult.length()-6) + temp + ".json";
                    if (!Files.exists(Path.of(pathResult))) break;
                }
            }
        }

        return pathResult;
    }
}