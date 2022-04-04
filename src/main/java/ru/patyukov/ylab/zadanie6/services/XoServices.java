package ru.patyukov.ylab.zadanie6.services;

import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie6.exceptions.XoException;
import ru.patyukov.ylab.zadanie6.model.Gameplay;
import ru.patyukov.ylab.zadanie6.model.game.Cell;
import ru.patyukov.ylab.zadanie6.model.game.Field;
import ru.patyukov.ylab.zadanie6.repository.XoRepository;
import ru.patyukov.ylab.zadanie6.model.ModelStatisticsPlayer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class XoServices {

    private final XoRepository xoRepository;   // Объект по работе со слоем Repository.

    // Главная страница.
    public String gameplay(GameXO gameXO, SessionStatus sessionStatus) {

        sessionStatus.setComplete();

        //  Записываем в переменную listPath класса gameXO список имен файлов с историей игры.
        /*
            Если возникнет ошибка при записи, то сохраняем в переменную строку "Не удалось получить список".
            Если файлов нет, то сохраним в переменную строку "пусто".
         */
        if (gameXO.createGameList() != 1) {
            gameXO.setListPath(new ArrayList<>(Arrays.asList("Не удалось получить список")));
        }
        else {
            if (gameXO.getListPath().size() == 0) {
                gameXO.setListPath(new ArrayList<>(Arrays.asList("пусто")));
            }
        }

        // Получаем список идентификаторов истории игры и имен игроков из БД.
        gameXO.setNameHistories(xoRepository.findByHistory());

        return "zadanie6/gameplay";
    }

    // Страница статистики.
    public String statisticsPlayer(GameXO gameXO) {

        // Записываем статистику в переменную statisticsArrayList класса StatisticsPlayer
        /*
            Если возникнет ошибка при записи, то сохраняем в переменную строку "Не удалось получить статистику".
         */

        gameXO.getStatisticsPlayer().setStatisticsArrayList(new ArrayList<>());   // Очищаем список.
        if (gameXO.getStatisticsPlayer().printStatisticsPlayer(false) != 1) {   // Записываем статистику в список.
            gameXO.getStatisticsPlayer().setStatisticsArrayList((
                    new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList("Не удалось получить статистику"))))));
        }

        // Статистика всех игроков из БД.
        gameXO.getStatisticsPlayer().setModelStatisticsPlayer(xoRepository.finaAllStat());

        return "zadanie6/statisticsplayer";
    }

    // Создаем игроков.
    public String playerSave(String namePlayer1, String value1, String namePlayer2, String value2, GameXO gameXO) {

        // Создаем игроков.
        if (gameXO.createPlayer(namePlayer1, value1, namePlayer2, value2) != 1) {
            return "zadanie6/createPlayer";
        }

        gameXO.queue();   // Определяем кто первым начнет.

        return "zadanie6/playNext";
    }

    // Очередной ход.
    public String playNext(GameXO gameXO, ArrayList<Field> fieldList, String xy) {
        int xNumber;
        int yNumber;

        if (xy.length() != 2) return "zadanie6/playNext";

        try {
            xNumber = Integer.parseInt(String.valueOf(xy.charAt(0)));
            yNumber = Integer.parseInt(String.valueOf(xy.charAt(1)));
        } catch (Exception e) {
            return "zadanie6/playNext";
        }

        // ИГРА НАЧАЛАСЬ.

        gameXO.setCount(gameXO.getCount() + 1);

        if (gameXO.getGameplay().getPlayer1().isStartStop()) {
            if (gameXO.goPlayer1(gameXO.getCount(), xNumber, yNumber) != 1) {  // Первый игрок делает очередной ход.
                return "zadanie6/playNext";
            }
        }
        else if (gameXO.getGameplay().getPlayer2().isStartStop()) {
            if (gameXO.goPlayer2(gameXO.getCount(), xNumber, yNumber) != 1) {   // Второй игрок делает очередной ход.
                return "zadanie6/playNext";
            }
        }

        // ПОДВОДИМ ИТОГИ ОЧЕРЕДНОГО ХОДА.
        String namePlayer = gameXO.getField().gameOverFinish();   // Получаем имя победителя, если такой есть. Иначе пустую строку.

        // Обрабатываем победителя, если он есть.
        if (!namePlayer.equals("")) {
            gameXO.finish(namePlayer);   // Обрабатываем победителя.
            gameXO.setFlag(false);

            // Сохраняем статистику в БД.
            if (namePlayer.equals(gameXO.getGameplay().getPlayer1().getName())) saveStatisticsPlayer(gameXO.getGameplay().getPlayer1().getName(), gameXO.getGameplay().getPlayer2().getName());
            else saveStatisticsPlayer(gameXO.getGameplay().getPlayer2().getName(), gameXO.getGameplay().getPlayer1().getName());

            // Сохраняем историю в БД.
            xoRepository.saveHistory(gameXO.getGameplay());
        }
        // Проверяем на ничью.
        if (!gameXO.getField().gameOver()) {
            gameXO.draw();   // Обрабатываем ничью.
            gameXO.setFlag(false);

            // Сохраняем историю в БД.
            xoRepository.saveHistory(gameXO.getGameplay());
        }

        return "zadanie6/playNext";
    }

    // Воспроизводим игру из файла.
    public String fileplay(MultipartFile file, ArrayList<Field> fieldList, GameXO gameXO) {
        if (file.getOriginalFilename().equals("")) {
            throw new XoException("Имя файла не введено");
        }
        else if ( !((file.getOriginalFilename().endsWith(".xml")) || (file.getOriginalFilename().endsWith(".json"))) ) {
            throw new XoException("Расширение выбранного файла не поддерживается");
        }

        try {
            InputStream inputStream = file.getInputStream();
            JSONParser parser = new JSONParser();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));   // Получаем буфер из полученного файла.
            JSONObject object = null;
            try {
                object = (JSONObject) parser.parse(bufferedReader);   // Получаем объект object из полученного буфера.
                gameXO.setGameplay(gameXO.getJsonSimpleParser().read(null, object));   // Получаем объект, который хранит историю игры.
            } catch (Exception e) {
                gameXO.setGameplay(gameXO.getXmlDomParser().read(null, file.getInputStream()));   // Получаем объект, который хранит историю игры.
            }
        } catch (Exception e) {
            throw new XoException("С файлом что то не так");
        }

        addFieldList(fieldList, gameXO);

        return "zadanie6/filePlay";
    }

    // Воспроизводим игру из файла по имении файла.
    public String nameFilePlay(ArrayList<Field> fieldList, GameXO gameXO, String namefile) {

        try {
            if (namefile.endsWith(".xml")) gameXO.setGameplay(gameXO.getXmlDomParser().read((gameXO.getPath() + namefile), null));
            else if (namefile.endsWith(".json")) gameXO.setGameplay(gameXO.getJsonSimpleParser().read((gameXO.getPath() + namefile), null));
            else if (namefile.equals("")) {
                throw new XoException("Имя файла не введено");
            } else throw new XoException("Расширение выбранного файла не поддерживается");
        } catch (Exception e) {
            if (e.getMessage().equals("Имя файла не введено")) throw new XoException("Имя файла не введено");
            if (e.getMessage().equals("Расширение выбранного файла не поддерживается")) throw new XoException("Расширение выбранного файла не поддерживается");
            throw new XoException("С файлом что то не так");
        }

        addFieldList(fieldList, gameXO);

        return "zadanie6/filePlay";
    }

    // Воспроизводим игру из БД по идентификационному номеру истории.
    public String historyIdPlay(ArrayList<Field> fieldList, GameXO gameXO, String historyID) {

        long id = -1;
        try {
            id = Integer.parseInt(historyID);
        } catch (Exception e) {
            throw new XoException("Введенные данные не являются числом");
        }
        if (id < 1) throw new XoException("Нет такой истории");

        Optional<Gameplay> gameplay = xoRepository.findByHistoryId(id);
        if (gameplay.isEmpty()) throw new XoException("Нет такой истории");
        else {
            Gameplay gp = gameplay.get();
            gameXO.setGameplay(gp);
        }

        addFieldList(fieldList, gameXO);

        return "zadanie6/filePlay";
    }


            // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ


    // Метод сохраняет статистику игры в БД.
    public void saveStatisticsPlayer(String namePlayerWon, String namePlayerLost) {

        // Метод сохраняет статистику игры в БД.
    /*
        Метод на вход получает имена игроков:
            namePlayerWon - выигравшего игрока;
            namePlayerLost - проигравшего игрока.
     */

        // Работаем со статистикой выигравшего игрока.
        Optional<ModelStatisticsPlayer> modelStatisticsPlayerWon = xoRepository.findByNameStat(namePlayerWon);   // Получаем статистику выигравшего игрока из БД.
        if (modelStatisticsPlayerWon.isEmpty()) {   // Если выигравшего игрока нет в статистике из БД, то добавляем.
            xoRepository.saveStat(new ModelStatisticsPlayer(namePlayerWon, 1, 0));
        } else {   // Иначе увеличиваем количество побед.
            ModelStatisticsPlayer buf = modelStatisticsPlayerWon.get();   // Достаем статистику выигравшего игрока из Optional.
            buf.setWon(buf.getWon() + 1);   // Увеличиваем количество побед.
            xoRepository.updateWonStat(buf);   // Сохраняем изменения в БД.
        }

        // Работаем со статистикой проигравшего игрока.
        Optional<ModelStatisticsPlayer> modelStatisticsPlayerLost = xoRepository.findByNameStat(namePlayerLost);   // Получаем статистику проигравшего игрока из БД.
        if (modelStatisticsPlayerLost.isEmpty()) {   // Если проигравшего игрока нет в статистике из БД, то добавляем.
            xoRepository.saveStat(new ModelStatisticsPlayer(namePlayerLost, 0, 1));
        } else {   // Иначе увеличиваем количество поражений.
            ModelStatisticsPlayer buf = modelStatisticsPlayerLost.get();   // Достаем статистику проигравшего игрока из Optional.
            buf.setLost(buf.getLost() + 1);   // Увеличиваем количество поражений.
            xoRepository.updateLostStat(buf);   // Сохраняем изменения в БД.
        }
    }

    // Метод заполняет список полей.
    private void addFieldList(ArrayList<Field> fieldList, GameXO gameXO) {

        // fieldList - Список полей. Каждое поле очередной ход. Сколько было ходов, столько и будет полей в списке.

        // Заполняем список полей пустыми полями, количество которых равно количеству ходов.
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) fieldList.add(new Field());

        Cell[] temp = new Cell[gameXO.getGameplay().sizeGame()];   // Массив всех клеток с ходами.

        // Проходим по каждому ходу. И заполняем массив клеток с ходами.
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) {

            Field buf = new Field();

            // Получаем массив клеток поля по координатам.
            temp[i] = buf.cell(gameXO.getGameplay().getGame().get(i).getX(), gameXO.getGameplay().getGame().get(i).getY());

            // По id игрока устанавливаем значение клетки (Х или О).
            if (gameXO.getGameplay().getPlayer1().getId().equals(gameXO.getGameplay().getGame().get(i).getPlayerId())) temp[i].setValue(gameXO.getGameplay().getPlayer1().getValue());
            else temp[i].setValue(gameXO.getGameplay().getPlayer2().getValue());
        }

        // Тут заполняется список полей. Каждое поле (каждый ход).
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) {   // Перебираем все поля из списка полей.
            /*
                Это описание к следующему циклу.

                Ищем клетку из массива клеток по координатам на поле.
                   - На 0 поле сохраним нулевой ход
                   - На 1 поле сохраним 0 и 1 ход
                   - На 2 поле сохраним 0, 1 и 3 ход
                   - И так далее
             */
            for (int l = 0, n = 0; (l < temp.length) && (n <= i); l++, n++) {
                for (int j = 0; j < 3; j++) {    // Тут перебираем все поле.
                    for (int k = 0; k < 3; k++) {   // Тут перебираем все поле.
                        if ((j == temp[l].getX()) && (k == temp[l].getY())) {   // Сохраняем клетку из массива клеток на поле.
                            Cell buf = fieldList.get(i).cell(j, k);
                            buf.setValue(temp[l].getValue());
                        }
                    }
                }
            }
        }
    }
}
