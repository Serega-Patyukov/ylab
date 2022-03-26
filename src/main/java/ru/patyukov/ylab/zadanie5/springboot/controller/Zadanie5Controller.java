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
import ru.patyukov.ylab.zadanie5.game.parser.json.JsonSimpleParser;
import ru.patyukov.ylab.zadanie5.game.model.Gameplay;
import ru.patyukov.ylab.zadanie5.game.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Controller
@RequestMapping("/gameplay")
@SessionAttributes({"fieldList", "gameplayList"})
public class Zadanie5Controller {

    @ModelAttribute("fieldList")   // Список полей. Каждое поле очередной ход. Сколько было ходов, столько и будет полей в списке.
    public ArrayList<Field> modelField() {
        return new ArrayList<>();
    }

    @ModelAttribute("gameplayList")  // Объект, который хранит историю игры.
    public ArrayList<Gameplay> modelGameplaye() {
        return new ArrayList<>();
    }



    // Главный метод.
    /*
        Начальная страница.
        Этот метод аналог метода gameResult() класса Main из консольных крестиков ноликов.
     */
    @GetMapping
    public String gameplay(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "zadanie5/gameplay";
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

    @GetMapping("/errorFile")
    public String errorFile(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "zadanie5/errorFile";
    }

    @GetMapping("/filePlay")
    public String fileplay(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "zadanie5/filePlay";
    }




    @PostMapping("/filePlay")
    public String fileplay(@RequestPart MultipartFile file,
                           @ModelAttribute ArrayList<Field> fieldList,
                           @ModelAttribute ArrayList<Gameplay> gameplay) {
        try {

            JSONParser parser = new JSONParser();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));   // Получаем буфер из полученного файла.
            JSONObject jsonObject = (JSONObject) parser.parse(bufferedReader);   // Получаем объект jsonObject из полученного буфера.
            JsonSimpleParser jsonSimpleParser = new JsonSimpleParser();   // Объект, который умеет парсить файл json.
            gameplay.add(jsonSimpleParser.read(null, jsonObject));   // Получаем объект, который хранит историю игры.

        } catch (Exception e) {
            System.out.println("\nError in Controller -> Zadanie5Controller -> fileplay");   // Сообщение для сервера.
            e.printStackTrace();
            return "redirect:/gameplay/errorFile";
        }

        // Заполняем список полей пустыми полями, количество которых равно количеству ходов.
        for (int i = 0; i < gameplay.get(0).sizeGame(); i++) fieldList.add(new Field());

        Cell[] temp = new Cell[gameplay.get(0).sizeGame()];   // Массив всех клеток с ходами.

        // Проходим по каждому ходу. И заполняем массив клеток с ходами.
        for (int i = 0; i < gameplay.get(0).sizeGame(); i++) {

            Field buf = new Field();

            // Получаем массив клеток поля по координатам.
            temp[i] = buf.cell(gameplay.get(0).getGame().get(i).getX(), gameplay.get(0).getGame().get(i).getY());

            // По id игрока устанавливаем значение клетки (Х или О).
            if (gameplay.get(0).getPlayer1().getId().equals(gameplay.get(0).getGame().get(i).getPlayerId())) temp[i].setValue(gameplay.get(0).getPlayer1().getValue());
            else temp[i].setValue(gameplay.get(0).getPlayer2().getValue());
        }

        // Тут заполняется список полей. Каждое поле (каждый ход).
        for (int i = 0; i < gameplay.get(0).sizeGame(); i++) {   // Перебираем все поля из списка полей.
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

        return "redirect:/gameplay/filePlay";
    }
}