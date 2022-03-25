package ru.patyukov.ylab.zadanie5.xo;

import java.util.Scanner;

public class Cell {

    private int x;
    private int y;
    private boolean ststus = true;//---------// Доступность клетки. true свободна. false занята.
    private String value = "-";//-----------// Отображение клетки. 1 из 3 символов '-', X, O.
    private String namePlayer = null;//----// Имя игрока, который владеет этой клеткой.

    // Конструктор.
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Метод получает клетку от игрока.
    /*
        На вход метод получает игрока player и размернось поля n.
        Если входные данные соответствуют требованиям (n >= 3) && (player != null),
        то метод возвтрвщает клетку.
        Иначе метод возвращает null.
     */
    public static Cell xy(Player player, int n) {
        Scanner scanner = new Scanner(System.in);

        // Вводимые играком координаты клетки.
        int x;
        int y;

        if ( !((n >= 3) && (player != null)) ) return null;

        while (true) {
            try {
                System.out.println("\tИгрок - " + player.getName());
                System.out.print("Введите х - ");
                x = Integer.parseInt(scanner.nextLine());
                System.out.print("Введите у - ");
                y = Integer.parseInt(scanner.nextLine());
                if ( !( (x >= 0) && (x < n) && (y >= 0) && (y < n) ) ) throw new Exception();
                return new Cell(x, y);   // Возвращаем клетку по указанным координатам.
            }
            catch (Exception c) {
                System.out.println("\nПовторите ввод !!!\n");
            }
        }
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

    public boolean isStstus() {
        return ststus;
    }
    public void setStstus(boolean ststus) {
        this.ststus = ststus;
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
