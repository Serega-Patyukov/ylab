package ru.patyukov.ylab.zadanie1;

import org.springframework.stereotype.Component;

@Component
public class NumberFibonacchi {

    // Рекурсивная реализация.
    public static int fib(int n) {
        if (n <= 1) return n;
        return fib(n - 1) + fib(n - 2);
    }

    // Рекурсивная реализация с использованием памяти.
    /*
        Устраняем повторы вызовов рекурсивного метода с одинаковыми значениями.
     */
    public static int fibMemor(int n, int[] memor) {
        if (memor[n] != -1) return memor[n];
        if (n <= 1) return n;
        memor[n] = fibMemor(n - 1, memor) + fibMemor(n - 2, memor);
        return memor[n];
    }

    // В цикле - с использованием памяти.
    /*
        Метод на вход получает индекс числа фибоначчи n.

            Метод сохраняет в массив array числа фибоначчи от 0 до n.
            И в массиве array по индексу n находит чсило фибоначчи.

        Метод возврвщвет число фибоначчии по индексу n.
     */
    public static int fibWhile(int n) {
        if (n <= 1) return n;
        int[] array = new int[n + 1];
        array[0] = 0;
        array[1] = 1;
        for (int i = 2; i < array.length; i++) {
            array[i] = array[i - 1] + array[i - 2];
        }
        return array[n];
    }

    // Сокращаем использование памяти, и алгоритм работает.
    /*
        Метод на вход получает индекс числа фибоначчи n.

            Метод сохраняет в массив int[] array new int[3] числа фибоначчи по следующему принципу:
                array[0] = n - 2;
                array[1] = n - 1;
                array[2] = n;
            Последний элемент в массиве array хранит чсило фибоначчи.

        Метод возврвщвет число фибоначчии по индексу n.
     */
    public static int fibWhileArrayLength3(int n) {
        if (n <= 1) return n;
        int[] array = new int[3];
        array[0] = 0;
        array[1] = 1;
        for (int i = 2; i < n + 1; i++) {
            array[2] = array[0] + array[1];
            if (i < n) {
                array[0] = array[1];
                array[1] = array[2];
            }
        }
        return array[array.length - 1];
    }
}