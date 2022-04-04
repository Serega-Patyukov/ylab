package ru.patyukov.ylab.zadanie6.services;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie6.model.game.Field;

import java.util.ArrayList;

public interface XoServicesInterf {

    // Главная страница.
    String gameplay(GameXO gameXO, SessionStatus sessionStatus);

    // Страница статистики.
    String statisticsPlayer(GameXO gameXO);

    // Создаем игроков.
    String playerSave(String namePlayer1, String value1, String namePlayer2, String value2, GameXO gameXO);

    // Очередной ход.
    String playNext(GameXO gameXO, ArrayList<Field> fieldList, String xy);

    // Воспроизводим игру из файла.
    String fileplay(MultipartFile file, ArrayList<Field> fieldList, GameXO gameXO);

    // Воспроизводим игру из файла по имении файла.
    String nameFilePlay(ArrayList<Field> fieldList, GameXO gameXO, String namefile);

    // Воспроизводим игру из БД по идентификационному номеру истории.
    String historyIdPlay(ArrayList<Field> fieldList, GameXO gameXO, String historyID);
}
