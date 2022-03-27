package ru.patyukov.ylab.zadanie5.game.parser;

import org.json.simple.JSONObject;
import ru.patyukov.ylab.zadanie5.game.model.Gameplay;

public interface InterfaceParser {
    int write(Gameplay gp, String path) throws Exception;
    Gameplay read(String path, Object object)  throws Exception;
}
