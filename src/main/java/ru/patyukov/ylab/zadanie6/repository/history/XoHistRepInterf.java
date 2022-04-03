package ru.patyukov.ylab.zadanie6.repository.history;

import ru.patyukov.ylab.zadanie6.model.game.modelGameplay.Gameplay;

import java.util.List;
import java.util.Optional;

public interface XoHistRepInterf {

    // Сохраняем историю игры.
    Gameplay saveHistory(Gameplay gameplay);

    // Получаем историю игры по historyID.
    Optional<Gameplay> findByHistoryId(Long historyID);

    // Получаем номер истории и имена игроков.
    List<String> findByHistory();
}