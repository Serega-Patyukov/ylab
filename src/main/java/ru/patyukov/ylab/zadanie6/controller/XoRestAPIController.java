package ru.patyukov.ylab.zadanie6.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie6.exceptions.XoException;
import ru.patyukov.ylab.zadanie6.model.Gameplay;
import ru.patyukov.ylab.zadanie6.model.ModelStatisticsPlayer;
import ru.patyukov.ylab.zadanie6.model.NameHistory;
import ru.patyukov.ylab.zadanie6.model.game.Field;
import ru.patyukov.ylab.zadanie6.model.game.Player;
import ru.patyukov.ylab.zadanie6.services.XoServicesInterf;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/gameplay/api")
@AllArgsConstructor
public class XoRestAPIController {

    private final XoServicesInterf xoServicesInterf;   // Объект по работе со слоем Services.

    @GetMapping("/help")
    public String help() {
        return "http://localhost:8080/gameplay/api/statisticsplayerBD - Статистика игроков. Хранится в БД.\n" +
                "http://localhost:8080/gameplay/api/statisticsplayerFile - Статистика игроков. Хранится в файле в локальном хранилище.\n" +
                "http://localhost:8080/gameplay/api/listPath - Имена файлов с историей игр. Файлы хранится в локальном хранилище.\n" +
                "http://localhost:8080/gameplay/api/nameFilePlay/{namefile} - Воспроизведение игры из файла по имении файла.\n" +
                "http://localhost:8080/gameplay/api/listNameHistory - Краткая информация об истории игр. Хранится в БД. Тут можно узнать идентификатор игры historyID.\n" +
                "http://localhost:8080/gameplay/api/historyIdPlay/{historyID} - Воспроизведение игры из БД по идентификационному номеру истории.\n" +
                "http://localhost:8080/gameplay/api/returnGameplay/{historyID} - Возвращение объекта истории из БД по идентификационному номеру.\n" +
                "http://localhost:8080/gameplay/api/fileplay - Воспроизведение игры из файла json или xml.\n" +
                "http://localhost:8080/gameplay/api/playerSave - Создание игроков и получение идентификационного номера присвоенного игре.\n" +
                "http://localhost:8080/gameplay/api/playNext/{historyID}/{xy} - Очередной ход. Оправка координат по идентификационному номеру";
    }

    // Статистика из БД.
    @GetMapping("/statisticsplayerBD")
    public List<ModelStatisticsPlayer> statisticsPlayerBD() {
        return xoServicesInterf.statisticsPlayerBD();
    }

    // Статистика из файла.
    @GetMapping("/statisticsplayerFile")
    public List<ModelStatisticsPlayer> statisticsPlayerFile() {
        return xoServicesInterf.statisticsPlayerFile();
    }

    // Имена файлов с историей игр, хранящихся в локальном хранилище.
    @GetMapping("/listPath")
    public List<String> listPath() {
        return xoServicesInterf.listPath();
    }

    // Воспроизводим игру из файла по имении файла.
    @GetMapping("/nameFilePlay/{namefile}")
    public List<Field> nameFilePlay(@PathVariable String namefile) {
        return xoServicesInterf.nameFilePlay(namefile);
    }

    // Краткая информация об истории игр. Получаем идентификатор истории игры, имена игроков и статус игры из БД.
    @GetMapping("/listNameHistory")
    public List<NameHistory> listNameHistory() {
        return xoServicesInterf.listNameHistory();
    }

    // Воспроизводим игру из БД по идентификационному номеру истории.
    @GetMapping("/historyIdPlay/{historyID}")
    public List<Field> historyIdPlay(@PathVariable long historyID) {
        return xoServicesInterf.historyIdPlay(historyID);
    }

    // Возвращаем объект истории из БД по идентификационному номеру истории.
    @GetMapping("/returnGameplay/{historyID}")
    public Gameplay returnGameplay(@PathVariable long historyID) {
        return xoServicesInterf.returnGameplay(historyID);
    }

    // Воспроизводим игру из файла.
    @PostMapping("/fileplay")
    public List<Field> fileplay(@RequestPart MultipartFile file) {
        return xoServicesInterf.fileplay(file);
    }

    // Создаем игроков.
    @PostMapping("/playerSave")
    public String playerSave(@RequestBody Player[] player) {
        return xoServicesInterf.playerSave(
                player[0].getName(), player[0].getValue(),
                player[1].getName(), player[1].getValue());
    }

    // Очередной ход.
    @PostMapping("/playNext/{historyID}/{xy}")
    public String playNext(@PathVariable Long historyID, @PathVariable String xy) {
        return xoServicesInterf.playNext(historyID, xy);
    }


            // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ


    // Ловим ошибки.
    @ExceptionHandler(XoException.class)
    public String handle(XoException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }
}
