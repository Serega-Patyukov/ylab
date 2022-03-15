package ru.patyukov.ylab.zadanie2;

public class Player {

    private boolean startStop = false;//---// Очередь вводить координаты. Если true, игрок вводит координаты, иначе координаты вводит другой игрок.
    private String value;//---------------// Х или О.
    private String name;//---------------// Имя игрока.

    // Конструктор.
    public Player(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean isStartStop() {
        return startStop;
    }
    public void setStartStop(boolean startStop) {
        this.startStop = startStop;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
