package ru.patyukov.ylab.zadanie5.game.parser.xml;

import org.w3c.dom.*;
import ru.patyukov.ylab.zadanie5.game.parser.InterfaceParser;
import ru.patyukov.ylab.zadanie5.game.GameResult;
import ru.patyukov.ylab.zadanie5.game.model.Gameplay;
import ru.patyukov.ylab.zadanie5.game.Player;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

// Класс по работе с xml файлом.
public class DomParser implements InterfaceParser {

    @Override
    public int write(Gameplay gp, String path) throws Exception {

        // Метод записывает и сохраняет xml файл.
    /*
        На вход метод получает строку с именем и директорией файла xml.
        И объект, который хранит историю игры.

        Если в работе метода возникнет ошибка, то метод вернет -1.
        Иначе 1.
     */

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element gameplay = document.createElement("Gameplay");

        Element player1 = document.createElement("Player");
        Element player2 = document.createElement("Player");

        Element game = document.createElement("Game");

        Element gameResult = document.createElement("GameResult");
        Element player = document.createElement("Player");

        document.appendChild(gameplay);

            gameplay.appendChild(player1);   // Игрок, который ходит первым.
                player1.setAttribute("id", gp.getPlayer1().getId());
                player1.setAttribute("name", gp.getPlayer1().getName());
                player1.setAttribute("symbol", gp.getPlayer1().getValue());

            gameplay.appendChild(player2);   // Игрок, который ходит вторым.
                player2.setAttribute("id", gp.getPlayer2().getId());
                player2.setAttribute("name", gp.getPlayer2().getName());
                player2.setAttribute("symbol", gp.getPlayer2().getValue());

            gameplay.appendChild(game);
                for (int i = 0; i < gp.sizeGame(); i++) {
                    Element step = document.createElement("Step");
                        game.appendChild(step);
                            step.setAttribute("num", gp.getStepGame(i).getNum());
                            step.setAttribute("playerId", gp.getStepGame(i).getPlayerId());
                            Text text = document.createTextNode(gp.getStepGame(i).returnXY());
                            step.appendChild(text);
                }

            gameplay.appendChild(gameResult);
                if (gp.getGameResult().getPlayer() == null) {
                    Text text = document.createTextNode("\n\t\tНичья\n\t");
                    gameResult.appendChild(text);
                }
                else {
                    gameResult.appendChild(player);
                    player.setAttribute("id", gp.getGameResult().getPlayer().getId());
                    player.setAttribute("name", gp.getGameResult().getPlayer().getName());
                    player.setAttribute("symbol", gp.getGameResult().getPlayer().getValue());
                }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream(path)));

        return 1;
    }

    @Override
    public Gameplay read(String path, Object object) throws Exception {

        // Метод считывает данные из файла xml и инициализирует объект класса который хранит историю игры.
        // На вход метод получает строку с именем и директорией файла xml или объект JSONObject.

        Gameplay gameplay;   // Объект для хранения истории игры.

        InputStream inputStream;

        if (path == null) {
            inputStream = (InputStream) object;
        }
        else {
            inputStream = new FileInputStream(path);
        }


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = documentBuilderFactory.newDocumentBuilder().parse(inputStream);

        Node nodeGameplay = document.getFirstChild();   // Получаем корневой node.

        // Тест.
        //System.out.println(nodeGameplay.getNodeName());

        NodeList nodeListGameplay = nodeGameplay.getChildNodes();   // Получаем список node из корневого node nodeGameplay.

        Node nodePlayer1 = null;
        Node nodePlayer2 = null;
        Node nodeGame = null;
        Node nodeGameResult = null;

        // Парсим каждый node из списка nodeListGameplay.
        for (int i = 0; i < nodeListGameplay.getLength(); i++) {

            if (nodeListGameplay.item(i).getNodeType() != Node.ELEMENT_NODE) continue;   // Убираем все лишнее.

            // Тест
            //System.out.println("\t" + nodeListGameplay.item(i).getNodeName());

            // Инициализируем плоя:
            /*
                nodePlayer1
                nodePlayer2
                nodeGame
                nodeGameResult
             */
            switch (nodeListGameplay.item(i).getNodeName()) {
                case "Player": {
                    if (nodePlayer1 == null) {
                        // Тест.
                        //System.out.println("\t\tполучили node nodePlayer1");
                        nodePlayer1 = nodeListGameplay.item(i);
                    }
                    else {
                        // Тест.
                        //System.out.println("\t\tполучили node nodePlayer2");
                        nodePlayer2 = nodeListGameplay.item(i);
                    }
                    break;
                }
                case "Game": {
                    nodeGame = nodeListGameplay.item(i);
                    // Тест.
                    //System.out.println("\t\tполучили node nodeGame");
                    break;
                }
                case "GameResult": {
                    nodeGameResult = nodeListGameplay.item(i);
                    // Тест.
                    //System.out.println("\t\tполучили node nodeGameResult");
                    break;
                }
            }
        }

        // Тест.
        //System.out.println(nodeGameplay.getNodeName());

        //==============================================================================================================
        // Парсим node nodePlayer1 и nodePlayer2.
        /*
         *
         *
         *
         *  *  *
         * * *
         * *
         */

        // Тест.
        //System.out.println("\nПарсим node nodePlayer1 и nodePlayer2");

        // Помещаем node nodePlayer1 и nodePlayer2 в список.
        ArrayList<Node> nodeArrayList = new ArrayList<>(Arrays.asList(nodePlayer1, nodePlayer2));

        ArrayList<Player> playerArrayList = new ArrayList<>();

        // Парсим node nodePlayer1 и nodePlayer2.
        for (int i = 0; i < nodeArrayList.size(); i++) {

            // Получаем список node атрибутов из node nodePlayer1 или nodePlayer2.
            NamedNodeMap nodeMapAttributes = nodeArrayList.get(i).getAttributes();

            // Тест.
            //System.out.println("\tnode" + nodeArrayList.get(i).getNodeName() + (i + 1));

            String symbol = "";
            String name = "";
            String id = "";

            // Парсим каждый node атрибут из списка nodeMapAttributes.
            for (int j = 0; j < nodeMapAttributes.getLength(); j++) {

                switch (nodeMapAttributes.item(j).getNodeName()) {
                    case "id": {
                        // Тест.
                        //System.out.println("\t\tполучили id");
                        id = nodeMapAttributes.item(j).getTextContent();
                        break;
                    }
                    case "name": {
                        // Тест.
                        //System.out.println("\t\tполучили name");
                        name = nodeMapAttributes.item(j).getTextContent();
                        break;
                    }
                    case "symbol": {
                        // Тест.
                        //System.out.println("\t\tполучили symbol");
                        symbol = nodeMapAttributes.item(j).getTextContent();
                        break;
                    }
                }
            }

            // Создаем игрока.
            Player player = new Player(name, symbol);
            player.setId(id);

            // Тест
            //System.out.print("\t" + player);

            // Добавляем созданного игрока в список.
            playerArrayList.add(player);
        }

        // Инициализируем объект для хранения истории игры.
        gameplay = new Gameplay(playerArrayList.get(0), playerArrayList.get(1));

        //==============================================================================================================
        // Парсим node Game.
        /*
         *
         *
         *
         *  *  *
         * * *
         * *
         */

        // Тест.
        //System.out.println("\nПарсим node nodeGame");

        NodeList nodeLisStep = nodeGame.getChildNodes();   // Получаем список node Step из node nodeGame.

        // Парсим каждый node Step из списка nodeLisStep.
        for (int i = 0; i < nodeLisStep.getLength(); i++) {

            if (nodeLisStep.item(i).getNodeType() != Node.ELEMENT_NODE) continue;   // Убираем все лишнее.

            NamedNodeMap nodeMapAttributes = nodeLisStep.item(i).getAttributes();   // Получаем список node атрибутов из node Step.
            NodeList nodeList = nodeLisStep.item(i).getChildNodes();      // Получаем список node из node Step.

            // Парсим каждый node из списка nodeList.
            for (int j = 0; j < nodeList.getLength(); j++) {

                String playerId = "";
                String num = "";

                // Парсим каждый node атрибут из списка nodeMapAttributes.
                for (int k = 0; k < nodeMapAttributes.getLength(); k++) {

                    switch (nodeMapAttributes.item(k).getNodeName()) {
                        case "playerId": {
                            playerId = nodeMapAttributes.item(k).getTextContent();
                            break;
                        }
                        case "num": {
                            num = nodeMapAttributes.item(k).getTextContent();
                            break;
                        }
                    }
                }

                // Получаем координаты х у.
                String xy = nodeList.item(j).getTextContent();
                int x = Integer.parseInt(String.valueOf(xy.charAt(0)));
                int y = Integer.parseInt(String.valueOf(xy.charAt(1)));

                gameplay.addGame(playerId, num, x, y);   // Добавляем очередной шаг.

                // Тест.
                // Выводим очередной объект Step.
                //System.out.print(gameplay.getGame(gameplay.getGameSize() - 1));
            }
        }

        //==============================================================================================================
        // Парсим node nodeGameResult.
        /*
         *
         *
         *
         *  *  *
         * * *
         * *
         */

        // Тест.
        //System.out.println("\nПарсим node nodeGameResult");

        // Получаем список node из node nodeGameResult.
        NodeList nodePlayer = nodeGameResult.getChildNodes();

        // Парсим каждый node из списка nodePlayer.
        for (int i = 0; i < nodePlayer.getLength(); i++) {

            // Если была ничья, то поле player класса GameResult инициализируем null. И выходим из цикла.
            if (nodePlayer.item(i).getTextContent().equals("\n\t\tНичья\n\t")) {
                GameResult gameResult = new GameResult();
                gameResult.setPlayer(null);
                gameplay.setGameResult(gameResult);
                break;
            }
            // Иначе убираем все лишнее и получаем список node атрибутов.

            if (nodePlayer.item(i).getNodeType() != Node.ELEMENT_NODE) continue;   // Убираем все лишнее.

            NamedNodeMap nodeMapAttributes = nodePlayer.item(i).getAttributes();   // Получаем список node атрибутов.

            // Парсим каждый node атрибут из списка nodeMapAttributes.
            for (int j = 0; j < nodeMapAttributes.getLength(); j++) {

                if (nodeMapAttributes.item(j).getNodeName().equals("id")) {
                    if (nodeMapAttributes.item(j).getTextContent().equals("1")) {
                        // Тест.
                        //System.out.println("\tполучили победителя");
                        gameplay.setGameResult(new GameResult(gameplay.getPlayer1()));
                    }
                    if (nodeMapAttributes.item(j).getTextContent().equals("2")) {
                        // Тест.
                        //System.out.println("\tполучили победителя");
                        gameplay.setGameResult(new GameResult(gameplay.getPlayer2()));
                    }
                }
            }
        }

        //System.out.println();
        //System.out.println(gameplay);

        return gameplay;
    }
}
