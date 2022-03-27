package ru.patyukov.ylab.zadanie5.springboot.controller;

import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie5.game.GameXO;
import ru.patyukov.ylab.zadanie5.game.Cell;
import ru.patyukov.ylab.zadanie5.game.Field;
import ru.patyukov.ylab.zadanie5.game.parser.json.JsonSimpleParser;
import ru.patyukov.ylab.zadanie5.game.model.Gameplay;
import ru.patyukov.ylab.zadanie5.game.Player;
import ru.patyukov.ylab.zadanie5.game.parser.xml.DomParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/gameplay")
@SessionAttributes({"fieldList", "gameXO"})
public class Zadanie5Controller {

    @ModelAttribute("fieldList")   // Список полей. Каждое поле очередной ход. Сколько было ходов, столько и будет полей в списке.
    public ArrayList<Field> modelField() {
        return new ArrayList<>();
    }

    @ModelAttribute("gameXO")   // Движок игры.
    public GameXO modelGameXO() {
        return new GameXO();
    }

    @GetMapping
    public String gameplay(@ModelAttribute GameXO gameXO, SessionStatus sessionStatus) {

        // Главный метод.
    /*
        Начальная страница.
        Этот метод аналог метода gameResult() класса TestGame из консольных крестиков ноликов.
     */

        if (gameXO.createGameList() != 1) gameXO.setStrListPath(new ArrayList<>(Arrays.asList("Не удалось вывеси список игр")));
        sessionStatus.setComplete();
        return "zadanie5/gameplay";
    }   // Главный метод.

    @GetMapping("/errorFile")
    public String errorFile(SessionStatus sessionStatus) {
        // Страница ошибок.
        sessionStatus.setComplete();
        return "zadanie5/errorFile";
    }   // Страница ошибок.

    @PostMapping("/filePlay")
    public String fileplay(@RequestPart MultipartFile file,
                           @ModelAttribute ArrayList<Field> fieldList,
                           @ModelAttribute GameXO gameXO) {

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

        return "redirect:/gameplay/filePlay";
    }

    @GetMapping("/nameFilePlay")
    public String nameFilePlay(@ModelAttribute ArrayList<Field> fieldList,
                               @ModelAttribute GameXO gameXO,
                               String namefile) {

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

    private void addFieldList(@ModelAttribute ArrayList<Field> fieldList,
                              @ModelAttribute GameXO gameXO) {
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

    @GetMapping("/filePlay")
    public String fileplay(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "zadanie5/filePlay";
    }







    @GetMapping("/createPlayer")
    public String play() {
        return "zadanie5/createPlayer";
    }

    @GetMapping("/playerSave")
    public String playerSave(String namePlayer1,
                             String value1,
                             String namePlayer2,
                             String value2,
                             @ModelAttribute ArrayList<Gameplay> gameplay,
                             @ModelAttribute ArrayList<Field> fieldList) {

        // Тут нужно сделать проверку.

        Player player1 = new Player(namePlayer1, value1);   // Создаем первого игрока.
        Player player2 = new Player(namePlayer2, value2);   // Создаем второго игрока.

        // Определяем кто первым начнет.
        /*
            Кто первый начнет у того id = "1".
            Кто второй начнет у того id = "2".
         */
        if ( ((int) ((Math.random()) * 10)) < 5 ) {
            player1.setStartStop(true);   // Начнет второй.
            player1.setId("1");   // Задаем id первого игрока.
            player2.setId("2");   // Задаем id второго игрока.
        }
        else {
            player2.setStartStop(true);   // Начнет первый.
            player2.setId("1");   // Задаем id второго игрока.
            player1.setId("2");   // Задаем id первого игрока.
        }

        gameplay.add(new Gameplay(player1, player2));

        // Добавляем пустое поле.
        fieldList.add(new Field());

        return "zadanie5/playNext";
    }

    // Этот метод аналог метода main() класса Main из консольных крестиков ноликов.
    @GetMapping("/playNext")
    public String playNext(@ModelAttribute ArrayList<Gameplay> gameplay,
                           @ModelAttribute ArrayList<Field> fieldList) {

        // тут проверку нужно сделать

        GameXO mainConsole = new GameXO();

        if (gameplay.get(0).getPlayer1().isStartStop()) {
            gameplay.get(0).getPlayer1().setStartStop(false);
            gameplay.get(0).getPlayer2().setStartStop(true);   // Второй игрок делает очередной ход.
        }
        else if (gameplay.get(0).getPlayer2().isStartStop()) {
            gameplay.get(0).getPlayer1().setStartStop(true);
            gameplay.get(0).getPlayer2().setStartStop(false);   // Первый игрок делает очередной ход.
        }

        // Добавляем пустое поле.
        fieldList.add(new Field());

        return "zadanie5/playNext";
    }
}