package ru.patyukov.ylab.zadanie5.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.patyukov.ylab.zadanie5.InterfaceParser;
import ru.patyukov.ylab.zadanie5.model.GameResult;
import ru.patyukov.ylab.zadanie5.model.Gameplay;
import ru.patyukov.ylab.zadanie5.model.Player;

import java.io.FileReader;
import java.io.FileWriter;

// Класс по работе с json файлом.
public class JsonSimpleParser implements InterfaceParser {

    // Метод записывает и сохраняет json файл.
    /*
        На вход метод получает строку с именем и директорией файла json.
        И объект, который хранит историю игры.
     */
    @Override
    public void write(Gameplay gp, String path) throws Exception {

        JSONObject jsonObject = new JSONObject();
            JSONObject jsonGameplay = new JSONObject();

                JSONObject jsonPlayer1 = new JSONObject();
                    jsonPlayer1.put("_id" , gp.getPlayer1().getId());
                    jsonPlayer1.put("_name", gp.getPlayer1().getName());
                    jsonPlayer1.put("_symbol", gp.getPlayer1().getValue());
                JSONObject jsonPlayer2 = new JSONObject();
                    jsonPlayer2.put("_id" , gp.getPlayer2().getId());
                    jsonPlayer2.put("_name", gp.getPlayer2().getName());
                    jsonPlayer2.put("_symbol", gp.getPlayer2().getValue());
                JSONArray jsonArrayPlayer = new JSONArray();
                jsonArrayPlayer.add(jsonPlayer1);
                jsonArrayPlayer.add(jsonPlayer2);


                JSONObject jsonGame = new JSONObject();
                    JSONArray jsonArrayStep = new JSONArray();
                        for (int i = 0; i < gp.gameSize(); i++) {
                            JSONObject jsonStep = new JSONObject();
                            jsonStep.put("_num", gp.getGame(i).getNum());
                            jsonStep.put("_playerId", gp.getGame(i).getPlayerId());
                            jsonStep.put("__text", gp.getGame(i).returnXY());
                            jsonArrayStep.add(jsonStep);
                        }
                jsonGame.put("Step", jsonArrayStep);

                JSONObject jsonGameResult = new JSONObject();
                    JSONObject jsonPlayer = new JSONObject();

                    if (gp.getGameResult().getPlayer() == null) {   // Если ничья.
                        jsonPlayer.put("null", "null");
                    }
                    else {
                        jsonPlayer.put("_id", gp.getGameResult().getPlayer().getId());
                        jsonPlayer.put("_name", gp.getGameResult().getPlayer().getName());
                        jsonPlayer.put("_symbol", gp.getGameResult().getPlayer().getValue());
                    }
                jsonGameResult.put("Player", jsonPlayer);

            jsonGameplay.put("Player", jsonArrayPlayer);
            jsonGameplay.put("Game", jsonGame);
            jsonGameplay.put("GameResult", jsonGameResult);
        jsonObject.put("Gameplay", jsonGameplay);

        try(FileWriter fileWriter = new FileWriter(path)) {
            jsonObject.writeJSONString(fileWriter);
            //fileWriter.write(jsonObject.toJSONString());
        }
    }

    // Метод считывает данные из файла json и инициализирует объект класса который хранит историю игры.
    // На вход метод получает строку с именем и директорией файла json или объект JSONObject.
    @Override
    public Gameplay read(String path, JSONObject object) throws Exception {

        Gameplay gameplay;   // Объект для хранения истории игры.

        JSONParser parser = new JSONParser();

        JSONObject jsonObject;

        if (object == null) {
            try(FileReader reader = new FileReader(path)) {
                jsonObject = (JSONObject) parser.parse(reader);
            }
        }
        else {
            jsonObject = object;
        }



        JSONObject jsonGameplay = (JSONObject) jsonObject.get("Gameplay");
        JSONArray jsonArrayPlayer = (JSONArray) jsonGameplay.get("Player");

        Player player1 = null;
        Player player2 = null;

        for (Object o : jsonArrayPlayer             ) {
            JSONObject player = (JSONObject) o;
            if (player.get("_id").equals("1")) {
                String name = (String) player.get("_name");
                String symbol = (String) player.get("_symbol");
                String id = (String) player.get("_id");
                player1 = new Player(name, symbol);
                player1.setId(id);
            }
            else {
                String name = (String) player.get("_name");
                String symbol = (String) player.get("_symbol");
                String id = (String) player.get("_id");
                player2 = new Player(name, symbol);
                player2.setId(id);
            }
        }

        gameplay = new Gameplay(player1, player2);

        JSONObject jsonGame = (JSONObject) jsonGameplay.get("Game");

        JSONArray jsonArrayStep = (JSONArray) jsonGame.get("Step");

        for (Object o : jsonArrayStep) {
            JSONObject jsonStep = (JSONObject) o;
            String playerId = (String) jsonStep.get("_playerId");
            String num = (String) jsonStep.get("_num");
            String xy = (String) jsonStep.get("__text");
            int x = Integer.parseInt(String.valueOf(xy.charAt(0)));
            int y = Integer.parseInt(String.valueOf(xy.charAt(1)));
            gameplay.setGame(playerId, num, x, y);
        }

        JSONObject jsonGameResult = (JSONObject) jsonGameplay.get("GameResult");

        JSONObject jsonPlayer = (JSONObject) jsonGameResult.get("Player");

        Player player = null;
        GameResult gameResult = new GameResult();
        gameResult.setPlayer(player);

        if (jsonPlayer.size() != 1) {      // Если есть победитель.
            String name = (String) jsonPlayer.get("_name");
            String symbol = (String) jsonPlayer.get("_symbol");
            String id = (String) jsonPlayer.get("_id");
            player = new Player(name, symbol);
            player.setId(id);
            gameResult.setPlayer(player);
        }

        gameplay.setGameResult(gameResult);

        return gameplay;
    }
}
