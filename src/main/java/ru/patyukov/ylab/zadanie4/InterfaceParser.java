package ru.patyukov.ylab.zadanie4;

import ru.patyukov.ylab.zadanie4.model.Gameplay;

public interface InterfaceParser {
    void write(Gameplay gp, String path) throws Exception;
    Gameplay read(String path)  throws Exception;
}
