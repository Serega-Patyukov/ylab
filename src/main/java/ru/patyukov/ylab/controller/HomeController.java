package ru.patyukov.ylab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String getHome() {
        return "home";
    }

    @PostMapping("/zadanie1")
    public String postZadanie1() {
        return "redirect:/zadanie1";
    }

    @GetMapping("/zadanie4")
    public String getZadanie4() {
        return "zadanie4/zadanieFour";
    }

    @PostMapping("/zadanie5")
    public String gettZadanie5() {
        return "redirect:/gameplay";
    }

    @PostMapping("/zadanie6")
    public String gettZadanie6() {
        return "redirect:/gameplay";
    }

}
