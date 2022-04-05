package ru.patyukov.ylab.zadanie6.model.game;

import lombok.Data;

@Data
public class Player {

    private boolean startStop = false;//---// Очередь вводить координаты. Если true, игрок вводит координаты, иначе координаты вводит другой игрок.
    private String value;//---------------// Х или О.
    private String name;//---------------// Имя игрока.
    private String id;

            // КОНСТРУКТОРЫ

    public Player() {}
    public Player(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
