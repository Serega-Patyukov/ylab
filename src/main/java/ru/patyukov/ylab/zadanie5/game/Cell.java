package ru.patyukov.ylab.zadanie5.game;

public class Cell {

    private int x;
    private int y;
    private boolean status = true;//---------// Доступность клетки. true свободна. false занята.
    private String value = "-";//-----------// Отображение клетки. 1 из 3 символов '-', X, O.
    private String namePlayer = null;//----// Имя игрока, который владеет этой клеткой.

            // КОНСТРУКТОРЫ

    public Cell() {}
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

            // МЕТОДЫ

    public static Cell xy(Player player, int n, int x, int y) {

        // Метод получает клетку от игрока.
    /*
        На вход метод получает игрока player и размерность поля n.
        Если входные данные соответствуют требованиям (n >= 3) && (player != null),
        то метод возвращает клетку.
        Иначе метод возвращает null.
     */

        if ( !((n >= 3) && (player != null)) ) return null;

        if ( !( (x >= 0) && (x < n) && (y >= 0) && (y < n) ) ) return null;

        return new Cell(x, y);   // Возвращаем клетку по указанным координатам.

    }   // Метод получает клетку от игрока.

            // GET SET

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

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getNamePlayer() {
        return namePlayer;
    }
    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }
}
