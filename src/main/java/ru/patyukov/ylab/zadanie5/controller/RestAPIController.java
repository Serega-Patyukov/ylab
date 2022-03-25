package ru.patyukov.ylab.zadanie5.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie5.json.JsonSimpleParser;
import ru.patyukov.ylab.zadanie5.model.Gameplay;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api")
public class RestAPIController {

    @PostMapping
    public String postRestAPIController(@RequestPart MultipartFile file) {

        try {
            JSONParser parser = new JSONParser();

            InputStream inputStream = file.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            JSONObject jsonObject = (JSONObject) parser.parse(bufferedReader);   // Получаем объект jsonObject из полученного файла.

            JsonSimpleParser jsonSimpleParser = new JsonSimpleParser();   // Объект, который умеет парсить файл json.

            Gameplay gameplay = jsonSimpleParser.read(null, jsonObject);   // Получаем объект, который хранит историю игры.

            gameplay.printGameplay();   // Воспроизводим игру.

            return "История игры воспроизведена на сервере";   // Сообщение для клиента.

        } catch (Exception e) {
            System.out.println("\nError in RestController postRestAPIController()");   // Сообщение для сервера.
            e.printStackTrace();
        }

        return "Error in RestController postRestAPIController()";   // Сообщение для клиента.
    }

    @PostMapping("/returnJSON")
    public Gameplay postReturnJSON(@RequestPart MultipartFile file) {

        Gameplay gameplay = null;

        try {
            JSONParser parser = new JSONParser();

            InputStream inputStream = file.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            JSONObject jsonObject = (JSONObject) parser.parse(bufferedReader);   // Получаем объект jsonObject из полученного файла.

            JsonSimpleParser jsonSimpleParser = new JsonSimpleParser();   // Объект, который умеет парсить файл json.

            gameplay = jsonSimpleParser.read(null, jsonObject);   // Получаем объект, который хранит историю игры.

        } catch (Exception e) {
            System.out.println("\nError in RestController postReturnJSON()");   // Сообщение для сервера.
            e.printStackTrace();
        }

        return gameplay;
    }

}
