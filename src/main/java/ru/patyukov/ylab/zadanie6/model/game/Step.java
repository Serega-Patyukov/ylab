package ru.patyukov.ylab.zadanie6.model.game;

// Объект класса хранит один ход.
public class Step {

    private long stepID;

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

            // GET SET

    public String getPlayerId() {
        return playerId;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public long getStepID() {
        return stepID;
    }
    public void setStepID(long stepID) {
        this.stepID = stepID;
    }
}
