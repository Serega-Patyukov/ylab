package ru.patyukov.ylab.zadanie6.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.game.modelGameplay.Gameplay;
import ru.patyukov.ylab.zadanie6.repository.history.XoHistRepInterf;
import ru.patyukov.ylab.zadanie6.repository.statistics.XoStatRepInterf;
import ru.patyukov.ylab.zadanie6.model.ModelStatisticsPlayer;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class XoRepository {

    private final XoStatRepInterf xoStatRepInterf;   // Объект для работы со статистикой.
    private final XoHistRepInterf xoHistRepInterf;   // Объект для работы с историей.

    // Получить статистику всех игроков.
    public List<ModelStatisticsPlayer> finaAllStat() {
        return xoStatRepInterf.finaAllStat();
    }

    // Получить статистику одного игрока.
    public Optional<ModelStatisticsPlayer> findByNameStat(String name) {
        return xoStatRepInterf.findByNameStat(name);
    }

    // Сохранить статистику одного игрока.
    public ModelStatisticsPlayer saveStat(ModelStatisticsPlayer modelStatisticsPlayer) {
        return xoStatRepInterf.saveStat(modelStatisticsPlayer);
    }

    // Обновить в статистике победы одного игрока.
    public ModelStatisticsPlayer updateWonStat(ModelStatisticsPlayer modelStatisticsPlayer) {
        return xoStatRepInterf.updateWonStat(modelStatisticsPlayer);
    }

    // Обновить в статистике поражения одного игрока.
    public ModelStatisticsPlayer updateLostStat(ModelStatisticsPlayer modelStatisticsPlayer){
        return xoStatRepInterf.updateLostStat(modelStatisticsPlayer);
    }

    // Сохраняем историю игры.
    public Gameplay saveHistory(Gameplay gameplay){
        return xoHistRepInterf.saveHistory(gameplay);
    }

    // Получаем историю игры по historyID.
    public Optional<Gameplay> findByHistoryId(Long historyID) {
        return xoHistRepInterf.findByHistoryId(historyID);
    }

    // Получаем идентификатор истории игры и имена игроков.
    public List<NameHistory> findByHistory() {
        return xoHistRepInterf.findByHistory();
    }
}
