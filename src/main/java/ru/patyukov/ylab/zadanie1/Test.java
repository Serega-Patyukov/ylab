package ru.patyukov.ylab.zadanie1;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {

        int n = 20;

        System.out.println("Рекурсивная реализация");
        System.out.println(n + " -> " + NumberFibonacchi.fib(n));

        System.out.println("Рекурсивная реализация с использованием памяти");
        int[] memor = new int[n + 1];
        Arrays.fill(memor, -1);
        System.out.println(n + " -> " + NumberFibonacchi.fibMemor(n, memor));

        System.out.println("В цикле - с использованием памяти");
        System.out.println(n + " -> " + NumberFibonacchi.fibWhile(n));

        System.out.println("Сокращаем использование памяти, и алгоритм работает");
        System.out.println(n + " -> " + NumberFibonacchi.fibWhileArrayLength3(n));

        System.out.println("Сокращаем использование памяти, сохраняем значения в кеш cache и алгоритм работает");
        System.out.println(n + " -> " + FibCache.fibWhileArrayLength3cache(n));
    }

}