package ru.patyukov.ylab.zadanie5.game;

// Объект класса хранит игрока победителя.
public class GameResult {
    private Player player = null;   // Игрок победитель.

            // КОНСТРУКТОРЫ

    public GameResult() {}
    public GameResult(Player player) {
        this.player = player;
    }

            // GET SET

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
}
