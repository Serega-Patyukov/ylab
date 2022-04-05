package ru.patyukov.ylab.zadanie6.repository.history;

import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.Gameplay;

import java.util.List;
import java.util.Optional;

public interface XoHistRepInterf {

    // Обновляем историю.
    Gameplay updateHistory(Gameplay gameplay);

    // Сохраняем историю игры.
    Gameplay saveHistory(Gameplay gameplay);

    // Получаем историю игры по historyID.
    Optional<Gameplay> findByHistoryId(Long historyID);

    // Получаем идентификатор истории игры, имена игроков и статус игры.
    List<NameHistory> findByHistory();
}
