package ru.patyukov.ylab.zadanie6.utils.parser;

import ru.patyukov.ylab.zadanie6.model.game.modelGameplay.Gameplay;

public interface InterfaceParser {
    int write(Gameplay gp, String path) throws Exception;
    Gameplay read(String path, Object object)  throws Exception;
}
