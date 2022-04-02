package ru.patyukov.ylab.zadanie6.game.parser;

import ru.patyukov.ylab.zadanie6.game.model.Gameplay;

public interface InterfaceParser {
    int write(Gameplay gp, String path) throws Exception;
    Gameplay read(String path, Object object)  throws Exception;
}
