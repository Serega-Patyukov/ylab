package ru.patyukov.ylab.zadanie5.model;

// Объект класса хранит один ход.
public class Step {

    private String playerId;  // id игрока.
    private String num;      // Номер хода.
    private int x;          // Координата х.
    private int y;         // Координата у.

    // Конструктор
    public Step(String playerId, String num, int x, int y) {
        this.playerId = playerId;
        this.num = num;
        this.x = x;
        this.y = y;
    }

    // Метод возвращает координаты х у в виде строки.
    public String returnXY() {

            /*
            Метод возвращает координаты х у в виде строки.
                Например, если
                    х = 1;
                    y = 2;
                то метод вернет "12"
            */

        return String.valueOf(x) + y;
    }
    public String getNum() {
        return num;
    }
    public String getPlayerId() {
        return playerId;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "\t\t<Step num=\"" + num + "\" playerId=\"" + playerId + "\">" + returnXY() + "</Step>\n";
    }
}
