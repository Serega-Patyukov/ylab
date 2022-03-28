package ru.patyukov.ylab.zadanie5.springboot.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.patyukov.ylab.zadanie5.game.GameXO;
import ru.patyukov.ylab.zadanie5.game.model.Gameplay;

@RestController
@RequestMapping("/api")
@SessionAttributes("gameXO")
public class RestAPIController {
    @GetMapping("/returnJSON")
    public Gameplay postReturnJSON(@ModelAttribute GameXO gameXO, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return gameXO.getGameplay();
    }
}
