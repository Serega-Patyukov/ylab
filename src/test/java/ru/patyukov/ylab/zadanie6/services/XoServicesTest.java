package ru.patyukov.ylab.zadanie6.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.patyukov.ylab.zadanie6.exceptions.XoException;
import ru.patyukov.ylab.zadanie6.model.Gameplay;
import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.game.Player;
import ru.patyukov.ylab.zadanie6.repository.XoRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
class XoServicesTest {

    @Autowired
    private XoServicesInterf xoServicesInterf;

    @Autowired
    private XoRepository xoRepository;

    @Test
    void playerSave() {

        String returnXoServ = "";
        try {
            String s1 = xoServicesInterf.playerSave("test1" , "Х", "test2", "О");   // Добавляем игру
            String s0 = xoServicesInterf.playerSave("test1" , "Х", "test2", "О");   // Пытаемся еще одну добавить.
        } catch (XoException e) {
            log.error("Поле занято. Ждите.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("Поле занято. Ждите.", returnXoServ);

        List<NameHistory> nameHistories = xoRepository.findByHistory();   //   Получаем идентификатор истории игры, имена игроков и статус игры.
        long historyID = nameHistories.get(nameHistories.size() - 1).getHistoryID();   // Из списка получаем последний идентификатор истории.
        Optional<Gameplay> optionalGameplay = xoRepository.findByHistoryId(historyID);   // Получаем игру из БД по идентификатору.
        Gameplay gameplay = optionalGameplay.get();
        gameplay.setStatus(true);   // Завершаем тестовую игру.
        xoRepository.updateHistory(gameplay);   // Обновляем игру в БД.

        try {
            String s1 = xoServicesInterf.playerSave("test1" , "Х", "test2", "Х");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - символы игроков должны быть разными. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - символы игроков должны быть разными. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("test1" , "О", "test1", "Х");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - имена игроков должны быть разными. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - имена игроков должны быть разными. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("tes t1" , "О", "test1", "Х");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - в имени первого игрока есть пробел. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - в имени первого игрока есть пробел. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("test1" , "О", "te st2", "Х");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - в имени второго игрока есть пробел. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - в имени второго игрока есть пробел. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("te" , "О", "test2", "Х");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - имя первого игрока < 3 символов. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - имя первого игрока < 3 символов. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("test1" , "f", "test2", "Х");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - символ первого игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - символ первого игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("test1" , "О", "te", "Х");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - имя второго игрока < 3 символов. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - имя второго игрока < 3 символов. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("test1" , "О", "test2", "ж");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - символ второго игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - символ второго игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);

        try {
            String s1 = xoServicesInterf.playerSave("test1" , "Х", "test2", "ж");   // Добавляем игру
        } catch (XoException e) {
            log.error("ОШИБКА - символ второго игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.");
            returnXoServ = e.getMessage();
        }
        Assertions.assertEquals("ОШИБКА - символ второго игрока != Х или О. Метод createPlayer() класса GameXO. Повторите запрос.", returnXoServ);
    }

    @Test
    void playNext() {
    }

    @Test
    void statisticsPlayerBD() {
    }

    @Test
    void statisticsPlayerFile() {
    }

    @Test
    void listNameHistory() {
    }

    @Test
    void historyIdPlay() {
    }

    @Test
    void returnGameplay() {
    }

    @Test
    void listPath() {
    }

    @Test
    void nameFilePlay() {
    }

    @Test
    void fileplay() {
    }
}