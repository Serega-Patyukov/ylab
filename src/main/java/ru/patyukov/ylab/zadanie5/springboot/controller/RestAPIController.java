package ru.patyukov.ylab.zadanie5.springboot.controller;

import org.springframework.web.bind.annotation.*;
import ru.patyukov.ylab.zadanie5.game.GameXO;
import ru.patyukov.ylab.zadanie5.game.model.Gameplay;

@RestController
@RequestMapping("/api")
@SessionAttributes("gameXO")
public class RestAPIController {
    @GetMapping("/returnJSON")
    public Gameplay postReturnJSON(@ModelAttribute GameXO gameXO) {
        return gameXO.getGameplay();
    }
}
