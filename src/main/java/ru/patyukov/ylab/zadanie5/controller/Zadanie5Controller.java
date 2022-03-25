package ru.patyukov.ylab.zadanie5.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie5.field.Cell;
import ru.patyukov.ylab.zadanie5.field.Field;
import ru.patyukov.ylab.zadanie5.json.JsonSimpleParser;
import ru.patyukov.ylab.zadanie5.model.Gameplay;

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

    @GetMapping
    public String getGameplay() {
        return "zadanie5/gameplay";
    }

    @GetMapping("/play")
    public String getPlay() {
        return "zadanie5/play";
    }

    @GetMapping("/errorfile")
    public String getErrorFile() {
        return "zadanie5/errorfile";
    }

    @GetMapping("/fileplay")
    public String getFileplay(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "zadanie5/fileplay";
    }

    @PostMapping("/fileplay")
    public String postFileplay(@RequestPart MultipartFile file,
                               @ModelAttribute ArrayList<Field> fieldList,
                               @ModelAttribute ArrayList<Gameplay> gameplay) {
        try {

            JSONParser parser = new JSONParser();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));   // Получаем буфер из полученного файла.
            JSONObject jsonObject = (JSONObject) parser.parse(bufferedReader);   // Получаем объект jsonObject из полученного буфера.
            JsonSimpleParser jsonSimpleParser = new JsonSimpleParser();   // Объект, который умеет парсить файл json.
            gameplay.add(jsonSimpleParser.read(null, jsonObject));   // Получаем объект, который хранит историю игры.

        } catch (Exception e) {
            System.out.println("\nError in Controller -> Zadanie5Controller -> postFileplay");   // Сообщение для сервера.
            e.printStackTrace();
            return "redirect:/gameplay/errorfile";
        }

        // Заполняем список полей пустыми полями, количество которых равно количеству ходов.
        for (int i = 0; i < gameplay.get(0).gameSize(); i++) fieldList.add(new Field());

        Cell[] temp = new Cell[gameplay.get(0).gameSize()];   // Массив всех клеток с ходами.

        // Проходим по каждому ходу. И заполняем массив клеток с ходами.
        for (int i = 0; i < gameplay.get(0).gameSize(); i++) {

            Field buf = new Field();

            // Получаем массив клеток поля по координатам.
            temp[i] = buf.cell(gameplay.get(0).getGame().get(i).getX(), gameplay.get(0).getGame().get(i).getY());

            // По id игрока устанавливаем значение клетки (Х или О).
            if (gameplay.get(0).getPlayer1().getId().equals(gameplay.get(0).getGame().get(i).getPlayerId())) temp[i].setValue(gameplay.get(0).getPlayer1().getValue());
            else temp[i].setValue(gameplay.get(0).getPlayer2().getValue());
        }

        // Тут заполняется список полей. Каждое поле (каждый ход).
        for (int i = 0; i < gameplay.get(0).gameSize(); i++) {   // Перебираем все поля из списка полей.
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

        return "redirect:/gameplay/fileplay";
    }
}