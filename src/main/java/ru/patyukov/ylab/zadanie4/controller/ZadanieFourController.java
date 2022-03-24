package ru.patyukov.ylab.zadanie4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/zadanieFour")
public class ZadanieFourController {

    @GetMapping
    public String getZadanieFourController() {
        return "/zadanie4/zadanieFour";
    }

}
