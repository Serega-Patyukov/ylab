package ru.patyukov.ylab.zadanie3.xml;

import ru.patyukov.ylab.zadanie3.xo.Player;

// Объект класса хранит игрока победителя.
public class GameResult {
    private Player player;   // Игрок победитель.

    // Конструктор.
    public GameResult(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "\t<GameResult>\n" +
                "\t" + (player == null ? "\t" + player + "\n" : player) +
                "\t</GameResult>";
    }
}
