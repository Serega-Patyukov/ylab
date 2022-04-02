package ru.patyukov.ylab.zadanie6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.patyukov.ylab.zadanie5.game.GameXO;
import ru.patyukov.ylab.zadanie5.springboot.controller.Zadanie5Controller;
import ru.patyukov.ylab.zadanie6.controller.Zadanie6Controller;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Zadanie5Controller.class)
public class TestZadanie6Controller {

    Zadanie6Controller zadanie6Controller = new Zadanie6Controller();

    @Autowired
    private MockMvc mockMvc;

    @Test   // Главная страница.
    public void test0Gameplay() throws Exception {
        mockMvc.perform(get("/gameplay"))
                .andExpect(status().isOk())
                .andExpect(view().name("zadanie5/gameplay"))
                .andExpect(content().string(containsString("Крестики нолики")))
                .andExpect(content().string(containsString("Человек против человека")))
                .andExpect(content().string(containsString("Играть")))
                .andExpect(content().string(containsString("Посмотреть статистику игроков")))
                .andExpect(content().string(containsString("Посмотреть")))
                .andExpect(content().string(containsString("Воспроизвести историю игры из json или xml файла")))
                .andExpect(content().string(containsString("Воспроизвести")))
                .andExpect(content().string(containsString("Воспроизвести историю игры из списка игр хранящихся на сервере")))
                .andExpect(content().string(containsString("Воспроизвести")))
                .andExpect(content().string(containsString("Список игр, хранящихся на сервере:")));

        GameXO gameXO = new GameXO();

        gameXO.setPath("***");
        Assertions.assertNotEquals(1, gameXO.createGameList());
        gameXO.setStrListPath(new ArrayList<>(Arrays.asList("Не удалось получить список")));
        Assertions.assertEquals("Не удалось получить список", gameXO.getStrListPath().get(0));


    }

    @Test   // Главная страница.
    public void test1Gameplay() throws Exception {
        mockMvc.perform(get("/gameplay"))
                .andExpect(status().isOk())
                .andExpect(view().name("zadanie5/gameplay"))
                .andExpect(content().string(containsString("Крестики нолики")))
                .andExpect(content().string(containsString("Человек против человека")))
                .andExpect(content().string(containsString("Играть")))
                .andExpect(content().string(containsString("Посмотреть статистику игроков")))
                .andExpect(content().string(containsString("Посмотреть")))
                .andExpect(content().string(containsString("Воспроизвести историю игры из json или xml файла")))
                .andExpect(content().string(containsString("Воспроизвести")))
                .andExpect(content().string(containsString("Воспроизвести историю игры из списка игр хранящихся на сервере")))
                .andExpect(content().string(containsString("Воспроизвести")))
                .andExpect(content().string(containsString("Список игр, хранящихся на сервере:")));

        GameXO gameXO = new GameXO();

        Assertions.assertEquals(1, gameXO.createGameList());

        gameXO.setStrListPath(new ArrayList<>());
        Assertions.assertEquals(0, gameXO.getStrListPath().size());

        gameXO.setStrListPath(new ArrayList<>(Arrays.asList("пусто")));

        Assertions.assertEquals("пусто", gameXO.getStrListPath().get(0));

    }

    @Test   // Страница ошибок.
    public void testErrorFile() throws Exception {
        mockMvc.perform(get("/gameplay/errorFile"))
                .andExpect(status().isOk())
                .andExpect(view().name("zadanie5/errorFile"))
                .andExpect(content().string(containsString("Крестики нолики")))
                .andExpect(content().string(containsString("Что-то пошло не так !!!")))
                .andExpect(content().string(containsString("Главная")));
    }

    @Test   // Страница добавления игроков.
    public void testCreatePlayer() throws Exception {
        mockMvc.perform(get("/gameplay/createPlayer"))
                .andExpect(status().isOk())
                .andExpect(view().name("zadanie5/createPlayer"))
                .andExpect(content().string(containsString("Крестики нолики")))
                .andExpect(content().string(containsString("Длины имен игроков должна быть больше 2 символов")))
                .andExpect(content().string(containsString("Имена игроков должны быть разными")))
                .andExpect(content().string(containsString("Символы игроков должны быть разными")))
                .andExpect(content().string(containsString("В имени игрока не должно быть пробелов")))
                .andExpect(content().string(containsString("Если после нажатия кнопки сохранить открылась эта же страничка,")))
                .andExpect(content().string(containsString("то вы ввели не правильные данные")))
                .andExpect(content().string(containsString("Введите имя первого игрока")))
                .andExpect(content().string(containsString("Выберите символ")))
                .andExpect(content().string(containsString("Введите имя второго игрока")))
                .andExpect(content().string(containsString("Выберите символ")))
                .andExpect(content().string(containsString("Сохранить")))
                .andExpect(content().string(containsString("Главная")));
    }

    @Test   // Страница хода.
    public void testGetPlayNext() throws Exception {
        mockMvc.perform(get("/gameplay/playNext"))
                .andExpect(status().isOk())
                .andExpect(view().name("zadanie5/playNext"))
                .andExpect(content().string(containsString("Крестики нолики")))
                .andExpect(content().string(containsString("Первый игрок:")))
                .andExpect(content().string(containsString("имя -")))
                .andExpect(content().string(containsString("символ")))
                .andExpect(content().string(containsString("Второй игрок:")))
                .andExpect(content().string(containsString("имя -")))
                .andExpect(content().string(containsString("символ -")))
                .andExpect(content().string(containsString("Главная")));
    }

    @Test   // Страница статистики.
    public void testStatisticsPlayer() throws Exception {
        mockMvc.perform(get("/gameplay/statisticsplayer"))
                .andExpect(status().isOk())
                .andExpect(view().name("zadanie5/statisticsplayer"))
                .andExpect(content().string(containsString("Крестики нолики")))
                .andExpect(content().string(containsString("Статистика игроков")))
                .andExpect(content().string(containsString("Главная")));
    }

    @Test   // Получаем представление объекта в json формате.
    public void testReturnJSON() throws Exception {
        mockMvc.perform(get("/api/returnJSON"));
    }

}
