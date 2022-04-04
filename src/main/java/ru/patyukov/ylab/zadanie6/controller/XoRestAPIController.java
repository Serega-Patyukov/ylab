package ru.patyukov.ylab.zadanie6.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.patyukov.ylab.zadanie6.services.GameXO;
import ru.patyukov.ylab.zadanie6.model.Gameplay;

@RestController
@RequestMapping("/api")
@SessionAttributes("gameXO")
public class XoRestAPIController {

    // Получаем представление объекта в json формате.
    @GetMapping("/returnJSON")
    public Gameplay returnJSON(@ModelAttribute GameXO gameXO, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return gameXO.getGameplay();
    }
}
