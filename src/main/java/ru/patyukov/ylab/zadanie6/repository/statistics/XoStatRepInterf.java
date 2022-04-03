package ru.patyukov.ylab.zadanie6.repository.statistics;

import ru.patyukov.ylab.zadanie6.model.ModelStatisticsPlayer;

import java.util.List;
import java.util.Optional;

public interface XoStatRepInterf {

    // Получить статистику всех игроков.
    List<ModelStatisticsPlayer> finaAllStat();

    // Получить статистику одного игрока.
    Optional<ModelStatisticsPlayer> findByNameStat(String name);

    // Сохранить статистику одного игрока.
    ModelStatisticsPlayer saveStat(ModelStatisticsPlayer modelStatisticsPlayer);

    // Обновить в статистике победы одного игрока.
    ModelStatisticsPlayer updateWonStat(ModelStatisticsPlayer modelStatisticsPlayer);

    // Обновить в статистике поражения одного игрока.
    ModelStatisticsPlayer updateLostStat(ModelStatisticsPlayer modelStatisticsPlayer);
}
