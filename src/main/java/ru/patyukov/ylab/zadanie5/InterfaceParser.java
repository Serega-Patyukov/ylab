package ru.patyukov.ylab.zadanie5;

import org.json.simple.JSONObject;
import ru.patyukov.ylab.zadanie5.model.Gameplay;

public interface InterfaceParser {
    void write(Gameplay gp, String path) throws Exception;
    Gameplay read(String path, JSONObject object)  throws Exception;
}
