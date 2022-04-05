package ru.patyukov.ylab.zadanie6.services;

import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie6.model.Gameplay;
import ru.patyukov.ylab.zadanie6.model.ModelStatisticsPlayer;
import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.game.Field;

import java.util.ArrayList;
import java.util.List;

public interface XoServicesInterf {

    // Статистика из БД.
    List<ModelStatisticsPlayer> statisticsPlayerBD();

    // Статистика из файла.
    List<ModelStatisticsPlayer> statisticsPlayerFile();

    // Создаем игроков.
    String playerSave(String namePlayer1, String value1, String namePlayer2, String value2);

    // Очередной ход.
    String playNext(Long historyID, String xy);

    // Получаем идентификатор истории игры, имена игроков и статус игры из БД.
    List<NameHistory> listNameHistory();

    // Воспроизводим игру из БД по идентификационному номеру истории.
    List<Field> historyIdPlay(long historyID);

    // Возвращаем объект истории из БД по идентификационному номеру истории.
    Gameplay returnGameplay(long historyID);

    // Имена файлов с историей игры хранящихся на диске.
    List<String> listPath();

    // Воспроизводим игру из файла по имении файла.
    List<Field> nameFilePlay(String namefile);

    // Воспроизводим игру из файла.
    List<Field> fileplay(MultipartFile file);
}
