package ru.patyukov.ylab.zadanie6.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.patyukov.ylab.zadanie6.exceptions.XoException;
import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.game.Cell;
import ru.patyukov.ylab.zadanie6.model.game.Field;
import ru.patyukov.ylab.zadanie6.model.game.GameResult;
import ru.patyukov.ylab.zadanie6.model.game.Player;
import ru.patyukov.ylab.zadanie6.model.Gameplay;
import ru.patyukov.ylab.zadanie6.utils.StatisticsPlayer;
import ru.patyukov.ylab.zadanie6.utils.parser.InterfaceParser;
import ru.patyukov.ylab.zadanie6.utils.parser.json.JsonSimpleParser;
import ru.patyukov.ylab.zadanie6.utils.parser.xml.XmlDomParser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Движок игры.
// Куча полезных методов.
@Data
@Slf4j
public class GameXO {

    private JsonSimpleParser jsonSimpleParser = new JsonSimpleParser();    // Объект класса, который сохраняет и читает файл json.
    private XmlDomParser xmlDomParser = new XmlDomParser();               // Объект класса, который сохраняет и читает файл xml.
    private InterfaceParser parser;                                      // Переменная которая может парсить json и xml.

    private StatisticsPlayer statisticsPlayer = new StatisticsPlayer(); // Статистика игры.

    private String path = "src/main/resources/static/file/zadanie6/";  // Относительный путь к файлам хранения истории.
    private ArrayList<String> listPath = new ArrayList<>();           // Список имен файлов с историей игр, без директории и расширения.

    private List<NameHistory> nameHistories = new ArrayList<>();   // Список идентификаторов истории игры и имен игроков из БД.

    private Gameplay gameplay = new Gameplay(new Player(), new Player());

    private Field field = new Field();    // создаем поле
    private int count = 0;             // Номер хода.

    public String pathJSON;        // Файл, который хранит историю игры в json.
    public String pathXML;      // Файл, который хранит историю игры в xml.

            // КОНСТРУКТОРЫ

    public GameXO() {  }
    public GameXO(Gameplay gameplay, Field field) {
        this.gameplay = gameplay;
        this.field = field;
    }

            // МЕТОДЫ

    public void createPlayer(String namePlayer1, String value1, String namePlayer2, String value2) {

        // Создаем игроков.

        /*
            Если создание игроков прошло успешно, то метод вернет 1.
            Иначе -1.
         */

        int lengthNamePlayer = 3;   // Длина имени игрока.

        Player player1 = null;
        Player player2 = null;

        // Символы должны быть разными.
        if (value1.equals(value2)) {
            throw new XoException("ОШИБКА - символы игроков должны быть разными. Метод createPlayer() класса GameXO. Повторите запрос.");
        }

        // Имена должны быть разными
        if (namePlayer1.equals(namePlayer2)) {
            throw new XoException("ОШИБКА - имена игроков должны быть разными. Метод createPlayer() класса GameXO. Повторите запрос.");
        }

        // Проверяем пробелы
        for (int i = 0; i < namePlayer1.length(); i++) {
            if (namePlayer1.charAt(i) == ' ') {
                throw new XoException("ОШИБКА - в имени первого игрока есть пробел. Метод createPlayer() класса GameXO. Повторите запрос.");
            }
        }
        for (int i = 0; i < namePlayer2.length(); i++) {
            if (namePlayer2.charAt(i) == ' ') {
                throw new XoException("ОШИБКА - в имени второго игрока есть пробел. Метод createPlayer() класса GameXO. Повторите запрос.");
            }
        }

        // Первый игрок.
        if (namePlayer1.length() < lengthNamePlayer) {
            throw new XoException("ОШИБКА - имя первого игрока < 3 символов. Метод createPlayer() класса GameXO. Повторите запрос.");
        }
        if (value1.equals("Х") || value1.equals("О")) player1 = new Player(namePlayer1, value1);   // Создаем первого игрока.
        else {
            throw new XoException("ОШИБКА - символ первого игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.");
        }

        // Второй игрок.
        if (namePlayer2.length() < lengthNamePlayer) {
            throw new XoException("ОШИБКА - имя второго игрока < 3 символов. Метод createPlayer() класса GameXO. Повторите запрос.");
        }
        if (value1.equals("О"))
            if (value2.equals("Х")) player2 = new Player(namePlayer2, "Х");   // Создаем второго игрока.
            else {
                throw new XoException("ОШИБКА - символ второго игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.");
            }
        else if (value1.equals("Х"))
            if (value2.equals("О")) player2 = new Player(namePlayer2, "О");   // Создаем второго игрока.
            else {
                throw new XoException("ОШИБКА - символ второго игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.");
            }
        else {
            throw new XoException("ОШИБКА - символ второго игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.");
        }

        gameplay.setPlayer1(player1);
        gameplay.setPlayer2(player2);

    }   // Создаем игроков.
    private String nameFile(String namePlayer1, String namePlayer2, InterfaceParser parser) {

        // Метод определяет имя файла для хранения истории.

        String pathResult = path + namePlayer1 + "_and_" + namePlayer2;   // Относительное имя файла.

        while (true) {

            // Для файлов .xml
            if (parser instanceof XmlDomParser) {
                if (!Files.exists(Path.of(pathResult))) {
                    pathResult =  pathResult + "_0" + ".xml";
                    if (!Files.exists(Path.of(pathResult))) {
                        try {
                            FileWriter fileWriter = new FileWriter(pathResult);   // Создали пустой файл.
                        } catch (IOException e) {
                            log.warn("ОШИБКА - не удалось создать пустой файл. Метод nameFile() класса GameXO");
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

    }        // Метод определяет имя файла для хранения истории.
    public int goPlayer1(int count, int x, int y) {

        // Первый игрок делает очередной ход.

        /*
            int count - номер хода.
            int x, int y - координаты.

            Если ход прошел удачно, то метод возвращает 1.
            Иначе -1.
         */

        Cell cell = Cell.xy(gameplay.getPlayer1(), field.getN(), x, y);   // Получаем клетку.
        if (cell != null) {
            if (field.saveCellInField(cell, gameplay.getPlayer1())) {
                gameplay.addGame(gameplay.getPlayer1().getId(), String.valueOf(count), cell.getX(), cell.getY());   // Добавляем очередной ход.
                gameplay.getPlayer1().setStartStop(false);
                gameplay.getPlayer2().setStartStop(true);
            }
            else return -1;
        }
        else return -1;
        return 1;

    }        // Первый игрок делает очередной ход.
    public int goPlayer2(int count, int x, int y) {

        // Второй игрок делает очередной ход.

        /*
            int count - номер хода.
            int x, int y - координаты.

            Если ход прошел удачно, то метод возвращает 1.
            Иначе -1.
         */

        Cell cell = Cell.xy(gameplay.getPlayer2(), field.getN(), x, y);
        if (cell != null) {
            if (field.saveCellInField(cell, gameplay.getPlayer2())) {
                gameplay.addGame(gameplay.getPlayer2().getId(), String.valueOf(count), cell.getX(), cell.getY());   // Добавляем очередной ход.
                gameplay.getPlayer1().setStartStop(true);
                gameplay.getPlayer2().setStartStop(false);
            }
            else return -1;
        }
        else return -1;
        return 1;

    }     // Второй игрок делает очередной ход.
    public void finish(String namePlayer) {

        // Метод обрабатывает победителя.

        // Сохраняем победителя в объект который хранит историю игры.
        if (gameplay.getPlayer1().getName().equals(namePlayer)) gameplay.setGameResult(new GameResult(gameplay.getPlayer1()));
        else gameplay.setGameResult(new GameResult(gameplay.getPlayer2()));

        // Сохраняем объект, который хранит историю игры.
        try {
            // Сохраняем объект, который хранит историю игры в файл xml.
            pathXML = nameFile(gameplay.getPlayer1().getName(), gameplay.getPlayer2().getName(), xmlDomParser);
            parser = xmlDomParser;
            parser.write(gameplay, pathXML);

            // Сохраняем объект, который хранит историю игры в файл json.
            pathJSON = nameFile(gameplay.getPlayer1().getName(), gameplay.getPlayer2().getName(), jsonSimpleParser);
            parser = jsonSimpleParser;
            parser.write(gameplay, pathJSON);
        } catch (Exception e) {
            log.warn("ОШИБКА - не удалось сохранить историю игры. Метод finish() класса GameXO");
        }

        // Сохраняем статистику.
        if (namePlayer.equals(gameplay.getPlayer1().getName())) statisticsPlayer.saveStatisticsPlayer(gameplay.getPlayer1().getName(), gameplay.getPlayer2().getName());
        else statisticsPlayer.saveStatisticsPlayer(gameplay.getPlayer2().getName(), gameplay.getPlayer1().getName());

    }          // Метод обрабатывает победителя.
    public int createGameList() {

        // Метод получает список имен предыдущих игр.

        /*
            Если возникнет ошибка в работе с файлами, то метод вернет -1.
            Иначе 1.
         */

        // Получаем список всех файлов из директории в которой хранится файлы с историей игр.
        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(Path.of(path));
            for (Path paht : paths) {   // Получаем список всех файлов.
                String strPaht = paht.getFileName().toString();   // Получаем имена файлов без директории.
                // Сохраняем только файлы с расширением xml и json.
                if (strPaht.endsWith(".xml")) listPath.add(strPaht);
                if (strPaht.endsWith(".json")) listPath.add(strPaht);
            }
        } catch (Exception e) {
            log.warn("ОШИБКА - не удалось загрузить историю игр предыдущих игроков. Метод createGameList() класса GameXO");
            return -1;
        }

        return 1;

    }                 // Метод получает список имен предыдущих игр.
    public void queue() {

        // Определяем кто первым начнет.

        /*
            Кто первый начнет у того id = "1".
            Кто второй начнет у того id = "2".
         */

        if ( ((int) ((Math.random()) * 10)) < 5 ) {
            gameplay.getPlayer1().setStartStop(true);   // Начнет первый.
            gameplay.getPlayer1().setId("1");   // Задаем id первого игрока.
            gameplay.getPlayer2().setId("2");   // Задаем id второго игрока.
        }
        else {
            gameplay.getPlayer2().setStartStop(true);   // Начнет второй.
            gameplay.getPlayer2().setId("1");   // Задаем id второго игрока.
            gameplay.getPlayer1().setId("2");   // Задаем id первого игрока.

            // Меняем местами игроков в классе Gameplay.
            // В поле Player player1 должен хранится игрок, который ходит первым.
            Player player = new Player(gameplay.getPlayer1().getName(), gameplay.getPlayer1().getValue());
            player.setId(gameplay.getPlayer1().getId());
            gameplay.setPlayer1(gameplay.getPlayer2());
            gameplay.setPlayer2(player);
        }
    }                      // Определяем кто первым начнет.
    public void draw() {

        // Метод обрабатывает ничью.

        GameResult gameResult = new GameResult();
        gameResult.setPlayer(null);

        gameplay.setGameResult(gameResult);   // Сохраняем null в объект который хранит историю игры.

        // Сохраняем объект, который хранит историю игры в файл.
        try {
            // Сохраняем объект, который хранит историю игры в файл xml.
            pathXML = nameFile(gameplay.getPlayer1().getName(), gameplay.getPlayer2().getName(), xmlDomParser);
            parser = xmlDomParser;
            parser.write(gameplay, pathXML);

            // Сохраняем объект, который хранит историю игры в файл json.
            pathJSON = nameFile(gameplay.getPlayer1().getName(), gameplay.getPlayer2().getName(), jsonSimpleParser);
            parser = jsonSimpleParser;
            parser.write(gameplay, pathJSON);
        } catch (Exception e) {
            log.warn("ОШИБКА - не удалось сохранить историю игры. Метод draw() класса GameXO");
        }
    }                    // Метод обрабатывает ничью.
}