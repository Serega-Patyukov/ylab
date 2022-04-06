package ru.patyukov.ylab.zadanie6.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NameHistory {

    private long historyID;        // Идентификатор истории.
    private boolean status;       // Состояние игры. false - игра не завершена. true - игра завершена.
    private String player1;      // Игрок, который ходит первым.
    private String id_1;        // id первого игрока
    private String player2;    // Игрок, который ходит вторым.
    private String id_2;       // id второго игрока
    private String victory;  // Имя победителя.

}
