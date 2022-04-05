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
@RequestMapping("/api")
@AllArgsConstructor
public class XoRestAPIController {

    private final XoServicesInterf xoServicesInterf;   // Объект по работе со слоем Services.


            // МЕТОДЫ GET


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

    // Получаем идентификатор истории игры, имена игроков и статус игры из БД.
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

    // Имена файлов с историей игры хранящихся на диске.
    @GetMapping("/listPath")
    public List<String> listPath() {
        return xoServicesInterf.listPath();
    }

    // Воспроизводим игру из файла по имении файла.
    @GetMapping("/nameFilePlay/{namefile}")
    public List<Field> nameFilePlay(@PathVariable String namefile) {
        return xoServicesInterf.nameFilePlay(namefile);
    }



            // МЕТОДЫ POST


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

    // Воспроизводим игру из файла.
    @PostMapping("/fileplay")
    public List<Field> fileplay(@RequestPart MultipartFile file) {
        return xoServicesInterf.fileplay(file);
    }


            // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ


    // Ловим ошибки.
    @ExceptionHandler(XoException.class)
    public String handle(XoException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }
}
