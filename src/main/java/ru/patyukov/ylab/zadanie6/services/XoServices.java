package ru.patyukov.ylab.zadanie6.services;

import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie6.exceptions.XoException;
import ru.patyukov.ylab.zadanie6.model.Gameplay;
import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.game.Cell;
import ru.patyukov.ylab.zadanie6.model.game.Field;
import ru.patyukov.ylab.zadanie6.model.game.GameResult;
import ru.patyukov.ylab.zadanie6.repository.XoRepository;
import ru.patyukov.ylab.zadanie6.model.ModelStatisticsPlayer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class XoServices implements XoServicesInterf {

    private final XoRepository xoRepository;   // Объект по работе со слоем Repository.

    // Создаем игроков
    @Override
    public String playerSave(String namePlayer1, String value1, String namePlayer2, String value2) {

        /*
        Метод на вход получат имена и символы игроков.

        Если при обработке полученных данных не произойдет выброса исключения XoException, то:
            - метод проверяет свободно ли поле. Если занято, то вернет соответствующее сообщение. Иначе
            - метод создает игру (объект который хранит историю игры), присваивает ей идентификационный номер historyID,
            и сохраняет созданную игру в БД.
        Иначе перехваченное исключение вернет описание ошибки.

        Метод вернет статус ОК.
        Метод вернет идентификационный номер historyID, по которому нужно будет обращаться в следующих запросах, для продолжения игры.
        Метод вернет имя игрока, который начинает играть.
         */

        // Проверяем доступность поля.
        List<NameHistory> nameHistories = xoRepository.findByHistory();   //   Получаем идентификатор истории игры, имена игроков и статус игры.
        if (nameHistories.size() > 0) {
            long historyID = nameHistories.get(nameHistories.size() - 1).getHistoryID();   // Из списка получаем последний идентификатор истории.
            Optional<Gameplay> optionalGameplay = xoRepository.findByHistoryId(historyID);   // Получаем игру из БД по идентификатору.
            if (!optionalGameplay.isEmpty()) {   // Если история есть
                if (!optionalGameplay.get().isStatus()) throw new XoException("Поле занято. Ждите."); // Если поле занято.
            }
        }


        GameXO gameXO = new GameXO();   // Создаем временный объект игрового движка.
        gameXO.getGameplay().setGameResult(new GameResult());   // Добавляем объект для хранения игрока победителя.

        gameXO.createPlayer(namePlayer1, value1, namePlayer2, value2);   // Создаем игроков.

        gameXO.queue();   // Определяем кто первым начнет.

        xoRepository.saveHistory(gameXO.getGameplay());   // Сохраняем в БД объект, который хранит историю игры.

        if (gameXO.getGameplay().getPlayer1().getId().equals("1")) return "" +
                "OK - Игроки созданы. " +
                "historyID - " + gameXO.getGameplay().getHistoryID() + ". " +
                "Начнет " + gameXO.getGameplay().getPlayer1().getName();
        else return  "" +
                "OK - Игроки созданы. " +
                "historyID - " + gameXO.getGameplay().getHistoryID() + ". " +
                "Начнет " + gameXO.getGameplay().getPlayer2().getName();
    }

    // Очередной ход.
    @Override
    public String playNext(Long historyID, String xy) {

        /*
        Метод на вход получает идентификационный номер игры (объекта который хранит историю) и координаты.

        По идентификационному номеру метод из БД получает игру (объекта который хранит историю).
        Если игры с полученным номером нет, то метод кидает исключение XoException с соответствующим сообщением.
        Если игра с полученным номером есть, но статус этой игры true, (что означает, что игра завершена),
        то метод кидает исключение XoException с соответствующим сообщением.

        Метод проверит координаты.
        Если возникнет ошибка, то метод кинет исключение XoException с соответствующим сообщением.

        Иначе игру можно продолжить.

        В процессе игры могут возникать исключения XoException.
        Перехватчик исключений вернет соответсвующее сообщение.

        По завершению очередного хода метод сохраняет историю в БД.
        И возвращает результат очередного хода в виде сообщения.
         */

        String result = "";   // Тут будет ответ метода.

        Optional<Gameplay> optionalGameplay = xoRepository.findByHistoryId(historyID);   // Получаем игру из БД.
        if (optionalGameplay.isEmpty()) throw new XoException("ОШИБКА - истории с указанным идентификационным номером не найдена. Повторите запрос.");
        if (optionalGameplay.get().isStatus()) throw new XoException("ОШИБКА - истории с указанным идентификационным номером завершена. Повторите запрос.");

        int xNumber;
        int yNumber;

        if (xy.length() != 2) throw new XoException("ОШИБКА - координаты должны состоять из 2 символов.  Повторите запрос.");

        try {
            xNumber = Integer.parseInt(String.valueOf(xy.charAt(0)));
            yNumber = Integer.parseInt(String.valueOf(xy.charAt(1)));
        } catch (Exception e) {
            throw new XoException("ОШИБКА - в координатах не должно быть символов отличных от цифр.  Повторите запрос.");
        }

        // ИГРА НАЧАЛАСЬ.

        GameXO gameXO = new GameXO();   // Создаем временный объект игрового движка.
        gameXO.getGameplay().setGameResult(new GameResult());   // Добавляем объект для хранения игрока победителя.

        gameXO.setGameplay(optionalGameplay.get());   // Помещаем историю игры в игровой движок.

        // Инициализируем поля движка.
        gameXO.setCount(gameXO.getGameplay().sizeGame() + 1);   // Задаем номер следующего шага.
        addFieldList(gameXO, false);   // Задаем поле.

        // Первый игрок делает очередной ход.
        if (gameXO.getGameplay().getPlayer1().isStartStop()) {
            if (gameXO.goPlayer1(gameXO.getCount(), xNumber, yNumber) != 1) {  // Первый игрок делает очередной ход.
                throw new XoException("ОШИБКА - первый игрок мухлюет. Его забанил хранитель кода. Пусть повторит запрос.");
            }
            result = "OK - игрок " + gameXO.getGameplay().getPlayer1().getName() + " сделал ход";
        }   // Второй игрок делает очередной ход.
        else if (gameXO.getGameplay().getPlayer2().isStartStop()) {
            if (gameXO.goPlayer2(gameXO.getCount(), xNumber, yNumber) != 1) {   // Второй игрок делает очередной ход.
                throw new XoException("ОШИБКА - второй игрок мухлюет. Его забанил хранитель кода. Пусть повторит запрос");
            }
            result =  "OK - игрок " + gameXO.getGameplay().getPlayer2().getName() + " сделал ход";
        }

        // ПОДВОДИМ ИТОГИ ОЧЕРЕДНОГО ХОДА.
        String namePlayer = gameXO.getField().gameOverFinish();   // Получаем имя победителя, если такой есть. Иначе пустую строку.

        // Обрабатываем победителя, если он есть.
        if (!namePlayer.equals("")) {
            gameXO.finish(namePlayer);   // Обрабатываем победителя.

            // Сохраняем статистику в БД.
            if (namePlayer.equals(gameXO.getGameplay().getPlayer1().getName())) saveStatisticsPlayer(gameXO.getGameplay().getPlayer1().getName(), gameXO.getGameplay().getPlayer2().getName());
            else saveStatisticsPlayer(gameXO.getGameplay().getPlayer2().getName(), gameXO.getGameplay().getPlayer1().getName());

            // Завершаем игру.
            gameXO.getGameplay().setStatus(true);

            result = "OK - Победил - " + namePlayer;
        }
        // Проверяем на ничью.
        if (!gameXO.getField().gameOver()) {
            gameXO.draw();   // Обрабатываем ничью.

            // Завершаем игру.
            gameXO.getGameplay().setStatus(true);

            result = "OK - Ничья";
        }

        // Обновляем историю в БД.
        xoRepository.updateHistory(gameXO.getGameplay());

        return result;
    }

    // Статистика из БД.
    @Override
    public List<ModelStatisticsPlayer> statisticsPlayerBD() {

        GameXO gameXO = new GameXO();

         // Статистика всех игроков из БД.
         gameXO.getStatisticsPlayer().setModelStatisticsPlayer(xoRepository.finaAllStat());

         if (gameXO.getStatisticsPlayer().getModelStatisticsPlayer().size() == 0) throw new XoException("В статистике пусто");

        return gameXO.getStatisticsPlayer().getModelStatisticsPlayer();
    }

    // Статистика из файла.
    @Override
    public List<ModelStatisticsPlayer> statisticsPlayerFile() {

        // Записываем статистику в переменную statisticsArrayList класса StatisticsPlayer
        /*
            Если возникнет ошибка при записи, то кидаем исключение.
         */

        GameXO gameXO = new GameXO();

        if (gameXO.getStatisticsPlayer().printStatisticsPlayer(false) != 1) {   // Записываем статистику в список.
            throw new XoException("ОШИБКА - Не удалось получить статистику");
        }

        if (gameXO.getStatisticsPlayer().getStatisticsArrayList().size() == 0) throw new XoException("В статистике пусто");

        return gameXO.getStatisticsPlayer().getStatisticsArrayList();
    }

    // Получаем идентификатор истории игры, имена игроков и статус игры из БД.
    @Override
    public List<NameHistory> listNameHistory() {
        if (xoRepository.findByHistory().size() == 0) throw new XoException("В истории пусто");
        return xoRepository.findByHistory();
    }

    // Воспроизводим игру из БД по идентификационному номеру истории.
    @Override
    public List<Field> historyIdPlay(long historyID) {

        if (historyID < 1) throw new XoException("ОШИБКА - недопустимое значение идентификационного номера истории игры");
        Optional<Gameplay> gameplay = xoRepository.findByHistoryId(historyID);
        if (gameplay.isEmpty()) throw new XoException("ОШИБКА - нет такой истории");

        GameXO gameXO = new GameXO();   // Создаем временный объект движка игры.
        gameXO.getGameplay().setGameResult(new GameResult());   // Создаем объект хранящий победителя.
        Gameplay gp = gameplay.get();   // Получаем историю игры из БД.
        gameXO.setGameplay(gp);   // Передаем историю игры игровому движку.

        return addFieldList(gameXO, true);
    }

    // Возвращаем объект истории из БД по идентификационному номеру истории.
    @Override
    public Gameplay returnGameplay(long historyID) {

        if (historyID < 1) throw new XoException("ОШИБКА - недопустимое значение идентификационного номера истории игры");
        Optional<Gameplay> gameplay = xoRepository.findByHistoryId(historyID);
        if (gameplay.isEmpty()) throw new XoException("ОШИБКА - нет такой истории");

        return gameplay.get();
    }

    // Имена файлов с историей игры хранящихся на диске.
    @Override
    public List<String> listPath() {

        //  Записываем в переменную strListPath класса gameXO список файлов с историей игры.

        GameXO gameXO = new GameXO();

        if (gameXO.createGameList() != 1) throw new XoException("ОШИБКА - Не удалось получить список");
        if (gameXO.getListPath().size() == 0) throw new XoException("Файлы с историей не найдены.");

        return gameXO.getListPath();
    }

    // Воспроизводим игру из файла по имении файла.
    @Override
    public List<Field> nameFilePlay(String namefile) {

        GameXO gameXO = new GameXO();

        try {
            if (namefile.endsWith(".xml")) gameXO.setGameplay(gameXO.getXmlDomParser().read((gameXO.getPath() + namefile), null));
            else if (namefile.endsWith(".json")) gameXO.setGameplay(gameXO.getJsonSimpleParser().read((gameXO.getPath() + namefile), null));
            else if (namefile.equals("")) {
                throw new XoException("ОШИБКА - Имя файла не введено");
            } else throw new XoException("ОШИБКА - Расширение выбранного файла не поддерживается");
        } catch (Exception e) {
            if (e.getMessage().equals("ОШИБКА - Имя файла не введено")) throw new XoException("ОШИБКА - Имя файла не введено");
            if (e.getMessage().equals("ОШИБКА - Расширение выбранного файла не поддерживается")) {
                throw new XoException("ОШИБКА - Расширение выбранного файла не поддерживается");
            }
            throw new XoException("ОШИБКА - С файлом что то не так");
        }
        return addFieldList(gameXO, true);
    }

    // Воспроизводим игру из файла.
    @Override
    public List<Field> fileplay(MultipartFile file) {

        GameXO gameXO = new GameXO();

        if (file.getOriginalFilename().equals("")) {
            throw new XoException("ОШИБКА - Имя файла не введено");
        }
        else if ( !((file.getOriginalFilename().endsWith(".xml")) || (file.getOriginalFilename().endsWith(".json"))) ) {
            throw new XoException("ОШИБКА - Расширение выбранного файла не поддерживается");
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
            throw new XoException("ОШИБКА - С файлом что то не так");
        }

        return addFieldList(gameXO, false);
    }


            // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ


    // Метод сохраняет статистику игры в БД.
    private void saveStatisticsPlayer(String namePlayerWon, String namePlayerLost) {

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

    // Метод заполняет поля.
    private List<Field> addFieldList(GameXO gameXO, boolean flag) {

        /*
        flag - true метод заполняет и возвращает список полей fieldList.
             - false метод заполняет и возвращает поле для игрового движка.
         */

        // Список полей. Каждое поле очередной ход. Сколько было ходов, столько и будет полей в списке.
        ArrayList<Field> fieldList = new ArrayList<>();

        // Заполняем список полей пустыми полями, количество которых равно количеству ходов.
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) fieldList.add(new Field());

        // Массив всех клеток с ходами. Тут задаем размер.
        Cell[] temp = new Cell[gameXO.getGameplay().sizeGame()];

        // Проходим по каждому ходу. И заполняем массив клеток с ходами.
        for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) {

            Field buf = new Field();

            // Получаем клетку поля.
            temp[i] = buf.cell(gameXO.getGameplay().getGame().get(i).getX(), gameXO.getGameplay().getGame().get(i).getY());

            // По id игрока устанавливаем значение переменных клетки.
            if (gameXO.getGameplay().getPlayer1().getId().equals(gameXO.getGameplay().getGame().get(i).getPlayerId())) {
                temp[i].setValue(gameXO.getGameplay().getPlayer1().getValue());   // Задаем символ.
                temp[i].setNamePlayer(gameXO.getGameplay().getPlayer1().getName());   // Задаем имя игрока который владеет клеткой.
                temp[i].setStatus(false);   // Делаем клетку занятой.
            }
            else {
                temp[i].setValue(gameXO.getGameplay().getPlayer2().getValue());   // Задаем символ.
                temp[i].setNamePlayer(gameXO.getGameplay().getPlayer2().getName());   // Задаем имя игрока который владеет клеткой.
                temp[i].setStatus(false);   // Делаем клетку занятой.
            }
        }

        if (flag) {
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
                                buf.setNamePlayer(temp[l].getNamePlayer());
                                buf.setStatus(false);
                            }
                        }
                    }
                }
            }
            return fieldList;
        } else {
            // Заполняем поле в игровом движке.
            List<Field> fieldListBuf = new ArrayList<>();
            Field field = new Field();
            for (int i = 0; i < gameXO.getGameplay().sizeGame(); i++) {
                Cell cell = temp[i];
                Cell cellField = field.cell(cell.getX(), cell.getY());
                cellField.setValue(cell.getValue());              // Задаем символ.
                cellField.setNamePlayer(cell.getNamePlayer());   // Задаем имя игрока который владеет клеткой.
                cellField.setStatus(false);                     // Делаем клетку занятой.
            }
            fieldListBuf.add(field);
            gameXO.setField(field);
            return fieldListBuf;
        }
    }
}
