package ru.patyukov.ylab.zadanie6.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NameHistory {

    private long historyID;     // Идентификатор истории.
    private String player1;    // Игрок, который ходит первым.
    private String player2;   // Игрок, который ходит вторым.

}
