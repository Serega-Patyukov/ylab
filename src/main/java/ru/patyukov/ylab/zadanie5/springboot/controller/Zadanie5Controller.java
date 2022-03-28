package ru.patyukov.ylab.zadanie5.springboot.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie5.game.GameXO;
import ru.patyukov.ylab.zadanie5.game.Cell;
import ru.patyukov.ylab.zadanie5.game.Field;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/gameplay")
@SessionAttributes({"fieldList", "gameXO"})
public class Zadanie5Controller {


            // МОДЕЛИ


    @ModelAttribute("fieldList")   // Список полей. Каждое поле очередной ход. Сколько было ходов, столько и будет полей в списке.
    public ArrayList<Field> modelField() {
        return new ArrayList<>();
    }

    @ModelAttribute("gameXO")   // Движок игры.
    public GameXO modelGameXO() {
        return new GameXO();
    }


            // МЕТОДЫ GET


    // Главная страница.
    @GetMapping
    public String gameplay(@ModelAttribute GameXO gameXO, SessionStatus sessionStatus, @ModelAttribute ArrayList<Field> fieldList) {

        sessionStatus.setComplete();

        if (gameXO.createGameList() != 1) gameXO.setStrListPath(new ArrayList<>(Arrays.asList("Не удалось получить список")));
        if (gameXO.getStrListPath().size() == 0) gameXO.setStrListPath(new ArrayList<>(Arrays.asList("пусто")));

        return "zadanie5/gameplay";
    }

    @GetMapping("/errorFile")
    public String errorFile() {
        return "zadanie5/errorFile";
    }

    @GetMapping("/statisticsplayer")
    public String statisticsPlayer(@ModelAttribute GameXO gameXO) {

        gameXO.getStatisticsPlayer().setStatisticsArrayList(new ArrayList<>());   // Очищаем список.
        gameXO.getStatisticsPlayer().printStatisticsPlayer(false);           // Записываем статистику в список.

        return "zadanie5/statisticsplayer";
    }

    @GetMapping("/createPlayer")
    public String createPlayer() {
        return "zadanie5/createPlayer";
    }

    @GetMapping("/playNext")
    public String playNext() {
        return "zadanie5/playNext";
    }


            // МЕТОДЫ POST


    @PostMapping("/playNext")
    public String playNext(@ModelAttribute GameXO gameXO, @ModelAttribute ArrayList<Field> fieldList, String xy) {
        int xNumber;
        int yNumber;

        try {
            xNumber = Integer.parseInt(String.valueOf(xy.charAt(0)));
            yNumber = Integer.parseInt(String.valueOf(xy.charAt(1)));
        } catch (Exception e) {
            return "zadanie5/playNext";
        }

        // ИГРА НАЧАЛАСЬ.

        gameXO.setCount(gameXO.getCount() + 1);

        if (gameXO.getGameplay().getPlayer1().isStartStop()) {
            if (gameXO.goPlayer1(gameXO.getCount(), xNumber, yNumber) != 1) {  // Первый игрок делает очередной ход.
                return "zadanie5/playNext";
            }
        }
        else if (gameXO.getGameplay().getPlayer2().isStartStop()) {
            if (gameXO.goPlayer2(gameXO.getCount(), xNumber, yNumber) != 1) {   // Второй игрок делает очередной ход.
                return "zadanie5/playNext";
            }
        }

        // ПОДВОДИМ ИТОГИ ОЧЕРЕДНОГО ХОДА.
        String namePlayer = gameXO.getField().gameOverFinish();   // Получаем имя победителя, если такой есть. Иначе пустую строку.

        // Обрабатываем победителя, если он есть.
        if (!namePlayer.equals("")) {
            gameXO.finish(namePlayer);   // Обрабатываем победителя.
            gameXO.setFlag(false);
        }
        // Проверяем на ничью.
        if (!gameXO.getField().gameOver()) {
            gameXO.draw();   // Обрабатываем ничью.
            gameXO.setFlag(false);
        }

        return "zadanie5/playNext";
    }

    @PostMapping("/playerSave")
    public String playerSave(String namePlayer1, String value1, String namePlayer2, String value2, @ModelAttribute GameXO gameXO,                             @ModelAttribute ArrayList<Field> fieldList) {

        // Создаем игроков.
        if (gameXO.createPlayer(namePlayer1, value1, namePlayer2, value2) != 1) {
            return "zadanie5/createPlayer";
        }

        gameXO.queue();   // Определяем кто первым начнет.

        return "zadanie5/playNext";
    }

    @PostMapping("/filePlay")
    public String fileplay(@RequestPart MultipartFile file, @ModelAttribute ArrayList<Field> fieldList, @ModelAttribute GameXO gameXO) {

        if (file.getOriginalFilename().equals("")) {
            System.out.println("\nError in Controller -> Zadanie5Controller -> fileplay()");   // Сообщение для сервера.
            return "redirect:/gameplay/errorFile";
        }
        else if ( !((file.getOriginalFilename().endsWith(".xml")) || (file.getOriginalFilename().endsWith(".json"))) ) {
            System.out.println("\nError in Controller -> Zadanie5Controller -> fileplay()");   // Сообщение для сервера.
            return "redirect:/gameplay/errorFile";
        }

        try {
            InputStream inputStream = file.getInputStream();
            JSONParser parser = new JSONParser();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));   // Получаем буфер из полученного файла.
            JSONObject object = null;
            try {
                object = (JSONObject) parser.parse(bufferedReader);   // Получаем объект object из полученного буфера.
                gameXO.setGameplay(gameXO.getJsonSimpleParser().read(null, object));   // Получаем объект, который хранит историю игры.
            } catch (Exception e) {
                gameXO.setGameplay(gameXO.getDomParser().read(null, file.getInputStream()));   // Получаем объект, который хранит историю игры.
            }
        } catch (Exception e) {
            System.out.println("\nError in Controller -> Zadanie5Controller -> fileplay()");   // Сообщение для сервера.
            return "redirect:/gameplay/errorFile";
        }

        addFieldList(fieldList, gameXO);

        return "zadanie5/filePlay";
    }

    @PostMapping("/nameFilePlay")
    public String nameFilePlay(@ModelAttribute ArrayList<Field> fieldList, @ModelAttribute GameXO gameXO, String namefile) {

        try {
            if (namefile.endsWith(".xml")) gameXO.setGameplay(gameXO.getDomParser().read((gameXO.getPath() + namefile), null));
            else if (namefile.endsWith(".json")) gameXO.setGameplay(gameXO.getJsonSimpleParser().read((gameXO.getPath() + namefile), null));
            else {
                System.out.println("\nError in Controller -> Zadanie5Controller -> nameFilePlay()");   // Сообщение для сервера.
                return "zadanie5/errorFile";
            }
        } catch (Exception e) {
            System.out.println("\nError in Controller -> Zadanie5Controller -> nameFilePlay()");   // Сообщение для сервера.
            return "zadanie5/errorFile";
        }

        addFieldList(fieldList, gameXO);

        return "zadanie5/filePlay";
    }


    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ


    private void addFieldList(@ModelAttribute ArrayList<Field> fieldList, @ModelAttribute GameXO gameXO) {
        // Заполняем список полей пустыми полями, количество которых равно количеству ходов.
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) fieldList.add(new Field());

        Cell[] temp = new Cell[gameXO.getGameplay().sizeGame()];   // Массив всех клеток с ходами.

        // Проходим по каждому ходу. И заполняем массив клеток с ходами.
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) {

            Field buf = new Field();

            // Получаем массив клеток поля по координатам.
            temp[i] = buf.cell(gameXO.getGameplay().getGame().get(i).getX(), gameXO.getGameplay().getGame().get(i).getY());

            // По id игрока устанавливаем значение клетки (Х или О).
            if (gameXO.getGameplay().getPlayer1().getId().equals(gameXO.getGameplay().getGame().get(i).getPlayerId())) temp[i].setValue(gameXO.getGameplay().getPlayer1().getValue());
            else temp[i].setValue(gameXO.getGameplay().getPlayer2().getValue());
        }

        // Тут заполняется список полей. Каждое поле (каждый ход).
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) {   // Перебираем все поля из списка полей.
            /*
                Это описание к следующему циклу.

                Ищем клетку из массива клеток по координатам на поле.
                   - На 0 поле сохраним нулевой ход
                   - На 1 поле сохраним 0 и 1 ход
                   - На 2 поле сохраним 0, 1 и 3 ход
                   - И так далее
             */
            for (int l = 0, n = 0; (l < temp.length) && (n <= i); l++, n++) {
                for (int j = 0; j < 3; j++) {    // Тут перебираем все поле.
                    for (int k = 0; k < 3; k++) {   // Тут перебираем все поле.
                        if ((j == temp[l].getX()) && (k == temp[l].getY())) {   // Сохраняем клетку из массива клеток на поле.
                            Cell buf = fieldList.get(i).cell(j, k);
                            buf.setValue(temp[l].getValue());
                        }
                    }
                }
            }
        }
    }
}