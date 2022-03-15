package ru.patyukov.ylab.zadanie1.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.patyukov.ylab.zadanie1.NumberFibonacchi;
import ru.patyukov.ylab.zadanie1.springboot.dopclass.NumberN;

@Controller
@RequestMapping("/zadanie1")
@SessionAttributes("numberN")
public class Zadanie1Controller {

    @ModelAttribute(name = "numberN")
    public NumberN numberN() {
        return new NumberN();
    }
    @ModelAttribute(name = "numberFibonacchi")
    public NumberFibonacchi numberFibonacchi() {
        return new NumberFibonacchi();
    }

    // Задание 1.
    @GetMapping
    public String getZadanie1() {
        return "/zadanie1/zadanie1";
    }

    // Скриншот страницы JavaRush.
    @GetMapping("/skrinshot")
    public String getSkrinshot() {
        return "/zadanie1/skrinshot";
    }
    @PostMapping("/skrinshot")
    public String postSkrinshot() {
        return "redirect:/zadanie1/skrinshot";
    }

    // Рекурсивная реализация с использованием памяти.
    @GetMapping("/rekyrsRealizMemoriz")
    public String getRekyrsRealizMemoriz() {
        return "/zadanie1/rekyrsRealizMemoriz";
    }
    @PostMapping("/rekyrsRealizMemoriz")
    public String postRekyrsRealizMemoriz() {
        return "redirect:/zadanie1/rekyrsRealizMemoriz";
    }
    @GetMapping("/rekyrsRealizMemorizTest")
    public String getRekyrsRealizMemorizTest(@ModelAttribute NumberN numberN) {
        return "/zadanie1/rekyrsRealizMemorizTest";
    }
    @PostMapping("/rekyrsRealizMemorizTest")
    public String postRekyrsRealizMemorizTest() {
        return "redirect:/zadanie1/rekyrsRealizMemorizTest";
    }
    @GetMapping("/rekyrsRealizMemorizTestRavno")
    public String getRekyrsRealizMemorizTestRavno(@ModelAttribute NumberN numberN) {
        return "/zadanie1/rekyrsRealizMemorizTestRavno";
    }
    @PostMapping("/rekyrsRealizMemorizTestRavno")
    public  String postRekyrsRealizMemorizTestRavno(@ModelAttribute NumberN numberN) {
        return "redirect:/zadanie1/rekyrsRealizMemorizTestRavno";
    }

    // В цикле - с использованием памяти.
    @GetMapping("/vCicleMemoriz")
    public String getVCicleMemoriz() {
        return "/zadanie1/vCicleMemoriz";
    }
    @PostMapping("/vCicleMemoriz")
    public String postVCicleMemoriz() {
        return "redirect:/zadanie1/vCicleMemoriz";
    }
    @GetMapping("/vCicleMemorizTest")
    public String getVCicleMemorizTest(@ModelAttribute NumberN numberN) {
        return "/zadanie1/vCicleMemorizTest";
    }
    @PostMapping("/vCicleMemorizTest")
    public String postVCicleMemorizTest() {
        return "redirect:/zadanie1/vCicleMemorizTest";
    }
    @GetMapping("/vCicleMemorizTestRavno")
    public String getVCicleMemorizTestRavno(@ModelAttribute NumberN numberN) {
        return "/zadanie1/vCicleMemorizTestRavno";
    }
    @PostMapping("/vCicleMemorizTestRavno")
    public  String postVCicleMemorizTestRavno(@ModelAttribute NumberN numberN) {
        return "redirect:/zadanie1/vCicleMemorizTestRavno";
    }


    // Сокращаем использование памяти, и алгоритм работает
    @GetMapping("/sokrMemoriz")
    public String getSokrMemoriz() {
        return "/zadanie1/sokrMemoriz";
    }
    @PostMapping("/sokrMemoriz")
    public String postSokrMemoriz() {
        return "redirect:/zadanie1/sokrMemoriz";
    }
    @GetMapping("/sokrMemorizTest")
    public String getSokrMemorizTest(@ModelAttribute NumberN numberN) {
        return "/zadanie1/sokrMemorizTest";
    }
    @PostMapping("/sokrMemorizTest")
    public String postSokrMemorizTest() {
        return "redirect:/zadanie1/sokrMemorizTest";
    }
    @GetMapping("/sokrMemorizTestRavno")
    public String getSokrMemorizTestRavno(@ModelAttribute NumberN numberN) {
        return "/zadanie1/sokrMemorizTestRavno";
    }
    @PostMapping("/sokrMemorizTestRavno")
    public  String postSokrMemorizTestRavno(@ModelAttribute NumberN numberN) {
        return "redirect:/zadanie1/sokrMemorizTestRavno";
    }

    // Сокращаем использование памяти, сохраняем значения в кеш cache и алгоритм работает.
    @GetMapping("/cache")
    public String getCache() {
        return "/zadanie1/cache";
    }
    @PostMapping("/cache")
    public String postCache() {
        return "redirect:/zadanie1/cache";
    }
    @GetMapping("/cacheTest")
    public String getCacheTest(@ModelAttribute NumberN numberN) {
        return "/zadanie1/cacheTest";
    }
    @PostMapping("/cacheTest")
    public String postCacheTest() {
        return "redirect:/zadanie1/cacheTest";
    }
    @GetMapping("/cacheTestRavno")
    public String getCacheTestRavno(@ModelAttribute NumberN numberN) {
        return "/zadanie1/cacheTestRavno";
    }
    @PostMapping("/cacheTestRavno")
    public  String postCacheTestRavno(@ModelAttribute NumberN numberN) {
        return "redirect:/zadanie1/cacheTestRavno";
    }

}