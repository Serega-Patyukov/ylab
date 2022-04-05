package ru.patyukov.ylab.zadanie6.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;

// Объект класса хранит игрока победителя.
@Data
@AllArgsConstructor
public class GameResult {

    private Player player = null;   // Игрок победитель.

            // КОНСТРУКТОРЫ

    public GameResult() {}
}
