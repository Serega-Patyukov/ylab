package ru.patyukov.ylab.zadanie6.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;

// Объект класса хранит один ход.
@Data
public class Step {

    private long stepID;       // Идентификационный номер хода.
    private String playerId;  // id игрока.
    private String num;      // Номер хода.
    private int x;          // Координата х.
    private int y;         // Координата у.

            // КОНСТРУКТОРЫ

    public Step() {}
    public Step(String playerId, String num, int x, int y) {
        this.playerId = playerId;
        this.num = num;
        this.x = x;
        this.y = y;
    }

            // МЕТОДЫ

    public String returnXY() {

        // Метод возвращает координаты х у в виде строки.

            /*
            Метод возвращает координаты х у в виде строки.
                Например, если
                    х = 1;
                    y = 2;
                то метод вернет "12"
            */

        return String.valueOf(x) + y;

    }   // Метод возвращает координаты х у в виде строки.
}
