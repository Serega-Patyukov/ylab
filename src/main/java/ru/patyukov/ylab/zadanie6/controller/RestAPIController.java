package ru.patyukov.ylab.zadanie6.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.patyukov.ylab.zadanie6.game.GameXO;
import ru.patyukov.ylab.zadanie6.game.model.Gameplay;

@RestController
@RequestMapping("/api")
@SessionAttributes("gameXO")
public class RestAPIController {

    // Получаем представление объекта в json формате.
    @GetMapping("/returnJSON")
    public Gameplay returnJSON(@ModelAttribute GameXO gameXO, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return gameXO.getGameplay();
    }
}
