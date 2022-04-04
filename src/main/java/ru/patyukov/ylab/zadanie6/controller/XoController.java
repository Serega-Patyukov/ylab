package ru.patyukov.ylab.zadanie6.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.patyukov.ylab.zadanie6.exceptions.XoException;
import ru.patyukov.ylab.zadanie6.model.game.Field;
import ru.patyukov.ylab.zadanie6.services.GameXO;
import ru.patyukov.ylab.zadanie6.services.XoServices;

import java.util.ArrayList;

@Controller
@RequestMapping("/gameplay")
@SessionAttributes({"fieldList", "gameXO"})
@AllArgsConstructor
@Slf4j
public class XoController {

    private final XoServices xoServices;   // Объект по работе со слоем Services.


            // МОДЕЛИ


    @ModelAttribute("fieldList")   // Список полей. Каждое поле очередной ход. Сколько было ходов, столько и будет полей в списке.
    public ArrayList<Field> modelField() {
        return new ArrayList<>();
    }

    @ModelAttribute("gameXO")   // Движок игры.
    public GameXO modelGameXO() {
        return new GameXO();
    }


            // МЕТОДЫ GET


    // Главная страница.
    @GetMapping
    public String gameplay(@ModelAttribute GameXO gameXO, SessionStatus sessionStatus) {
        return xoServices.gameplay(gameXO, sessionStatus);
    }

    // Страница статистики.
    @GetMapping("/statisticsplayer")
    public String statisticsPlayer(@ModelAttribute GameXO gameXO) {
        return xoServices.statisticsPlayer(gameXO);
    }

    // Страница ошибок.
    @GetMapping("/errorFile")
    public String errorFile() {
        return "zadanie6/errorFile";
    }

    // Страница добавления игроков.
    @GetMapping("/createPlayer")
    public String createPlayer() {
        return "zadanie6/createPlayer";
    }

    // Страница хода.
    @GetMapping("/playNext")
    public String playNext() {
        return "zadanie6/playNext";
    }


            // МЕТОДЫ POST


    // Создаем игроков.
    @PostMapping("/playerSave")
    public String playerSave(String namePlayer1, String value1, String namePlayer2, String value2, @ModelAttribute GameXO gameXO) {
        return xoServices.playerSave(namePlayer1, value1, namePlayer2, value2, gameXO);
    }

    // Очередной ход.
    @PostMapping("/playNext")
    public String playNext(@ModelAttribute GameXO gameXO, @ModelAttribute ArrayList<Field> fieldList, String xy) {
        return xoServices.playNext(gameXO, fieldList, xy);
    }

    // Воспроизводим игру из файла.
    @PostMapping("/filePlay")
    public String fileplay(@RequestPart MultipartFile file, @ModelAttribute ArrayList<Field> fieldList, @ModelAttribute GameXO gameXO) {
        return xoServices.fileplay(file, fieldList, gameXO);
    }

    // Воспроизводим игру из файла по имении файла.
    @PostMapping("/nameFilePlay")
    public String nameFilePlay(@ModelAttribute ArrayList<Field> fieldList, @ModelAttribute GameXO gameXO, String namefile) {
        return xoServices.nameFilePlay(fieldList, gameXO, namefile);
    }

    // Ловим ошибки.
    @ExceptionHandler(XoException.class)
    public String handle(XoException e) {
        log.error(e.getMessage());
        return "zadanie6/errorFile";
    }
}