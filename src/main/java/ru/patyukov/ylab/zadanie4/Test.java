package ru.patyukov.ylab.zadanie4;

import ru.patyukov.ylab.zadanie4.json.JsonSimpleParser;
import ru.patyukov.ylab.zadanie4.model.Gameplay;
import ru.patyukov.ylab.zadanie4.xml.DomParser;

public class Test {
    public static void main(String[] args) throws Exception {

        String path = "src/main/resources/static/file/zadanie4/Артем_and_Сергей.xml";

        DomParser domParser = new DomParser();
        Gameplay gameplay = domParser.read(path, null);
        System.out.println(gameplay);

        System.out.println("==========================================================");

        JsonSimpleParser jsonSimpleParser = new JsonSimpleParser();

        jsonSimpleParser.write(gameplay, "src/main/resources/static/file/zadanie4/test.json");

        gameplay = jsonSimpleParser.read("src/main/resources/static/file/zadanie4/test.json", null);
        System.out.println(gameplay);
    }
}
