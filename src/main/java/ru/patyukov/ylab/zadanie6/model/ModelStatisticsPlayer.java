package ru.patyukov.ylab.zadanie6.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelStatisticsPlayer {

    private String name;   // Имя игрока.
    private int won;      // Количество побед.
    private int lost;    // Количество поражений.

}
