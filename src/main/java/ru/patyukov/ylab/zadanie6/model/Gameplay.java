package ru.patyukov.ylab.zadanie6.model;
import lombok.Data;
import ru.patyukov.ylab.zadanie6.model.game.*;

import java.util.ArrayList;

// Объект класса хранит историю игры. И тут есть метод для вывода в консоль истории игры.
@Data
public class Gameplay {

    private boolean status = false;    // Состояние игры. false - игра не завершена. true - игра завершена.
    private long historyID;         // Идентификационный номер игры
    private Player player1;      // Игрок, который ходит первым.
    private Player player2;   // Игрок, который ходит вторым.

    private ArrayList<Step> game = new ArrayList<>();   // Список ходов.
                                                       // 0 элемент - ход первого игрока.
                                                      // 1 элемент - ход второго игрока.
                                                     // 2 элемент - ход первого игрока.
                                                    // 3 элемент - ход второго игрока.
                                                   // ...
    private GameResult gameResult;   // Игрок победитель.

            // КОНСТРУКТОРЫ

    public Gameplay() {}
    public Gameplay(Player player1, Player player2) {
        this.player1 = player1;    // Игрок, который ходит первым.
        this.player2 = player2;   // Игрок, который ходит вторым.
    }

            // МЕТОДЫ

    public void addGame(String playerId, String num, int x, int y) {

        // Метод добавляет ход в список ходов.

        game.add(new Step(playerId, num, x, y));

    }   // Метод добавляет ход в список ходов.
    public Step getStepGame(int i) {

        // Метод возвращает один ход по индексу i.

        return game.get(i);

    }    // Метод возвращает один ход по индексу i.
    public void printGameplay() {

        // Метод выводит в консоль историю игры.

        Field field = new Field();   // Создаем поле.

        // Выводим каждый ход.
        for (int i = 0; i < game.size(); i++) {

            // Получаем клетку поля по координатам.
            Cell cell = field.cell(game.get(i).getX(), game.get(i).getY());

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

    }    // Метод выводит в консоль историю игры.
    public int sizeGame() {

        // Количество ходов.

        return game.size();

    }       // Количество ходов.
}
