package ru.patyukov.ylab.zadanie5.model;

import ru.patyukov.ylab.zadanie5.xo.Cell;
import ru.patyukov.ylab.zadanie5.xo.Field;
import ru.patyukov.ylab.zadanie5.xo.Player;

import java.util.ArrayList;

// Объект класса хранит историю игры. И есть метод для вывода на экран истории игры.
public class Gameplay {
    private Player player1;    // Игрок который ходит первым.
    private Player player2;   // Игрок который ходит вторым.

    private ArrayList<Step> game = new ArrayList<>();   // Список ходов.
                                                    // 0 элемент - ход первого игрока.
                                                    // 1 элемент - ход второго игрока.
                                                    // 2 элемент - ход первого игрока.
                                                    // 3 элемент - ход второго игрока.
                                                    // ...
    private GameResult gameResult;   // Игрок победитель.

    // Конструктор.
    public Gameplay(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    // Метод выводит на экран историю игры.
    public void printGameplay() {

        Field field = new Field();   // Создаем поле.

        // Выводим каждый ход.
        for (int i = 0; i < game.size(); i++) {

            // Получаем клетку поля по координатам.
            Cell cell = field.getCell(game.get(i).getX(), game.get(i).getY());

            // По id игрока устанавливаем значение клетки (Х или О).
            if (player1.getId().equals(game.get(i).getPlayerId())) cell.setValue(player1.getValue());
            else cell.setValue(player2.getValue());

            System.out.print("\nХод - " + (i + 1));

            // Выводим поле
            field.printFiled();
        }

        // Выводим победителя или Ничья.
        if (gameResult.getPlayer() != null) System.out.println("\nPlayer " + gameResult.getPlayer().getId() + " -> "
                + gameResult.getPlayer().getName() + " is winner as '" + gameResult.getPlayer().getValue() + "'!");
        else System.out.println("\nНИЧЬЯ !!!");

        System.out.println("=========================================================\n\n");

    }

    // Колличество ходов.
    public int gameSize() {
        return game.size();
    }

    // Метод добавляет ход в список ходов.
    public void setGame(String playerId, String num, int x, int y) {
        game.add(new Step(playerId, num, x, y));
    }
    // Метод возвращает один ход по индексу i.
    public Step getGame(int i) {
        return game.get(i);
    }

    // Метод написан для метода toString()
    public String printgame() {
        String s ="";
        s = "\t<Game>\n";
        for (int i = 0; i < gameSize(); i++) {
            s += getGame(i);
        }
        s +="\t</Game>\n";
        return s;
    }

    @Override
    public String toString() {
        return "\nВывод на экран объекта класса Gameplay\n" +
                "Это сделано для тестирования !!!\n" +
                "<Gameplay>\n" +
                player1 +
                player2 +
                printgame() +
                gameResult +
                "\n</Gameplay>\n";
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public ArrayList<Step> getGame() {
        return game;
    }
    public void setGame(ArrayList<Step> game) {
        this.game = game;
    }

    public GameResult getGameResult() {
        return gameResult;
    }
    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }
}
