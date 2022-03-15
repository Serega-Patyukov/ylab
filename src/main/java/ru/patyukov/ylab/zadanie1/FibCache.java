package ru.patyukov.ylab.zadanie1;

import com.google.common.cache.*;
import java.util.concurrent.TimeUnit;

public class FibCache {

    // Создаем кеш с использованием библиотеки guava.
    private static Cache<String, Integer> cache = CacheBuilder.newBuilder()
            .initialCapacity(3)   // количество записей в кеше.
            .concurrencyLevel(1)   // количество потоком, котрые могут одновременно модифицировать кеш.
//            .removalListener(new RemovalListener<String, Integer>() {   // метод автоматически вызывается в момент удаления записи.
//                @Override
//                public void onRemoval(RemovalNotification<String, Integer> removalNotification) {
//                    // выводим информацию об удаляемой записи.
//                    System.out.println("Removed entry: " + removalNotification.getKey() + " -> " + removalNotification.getValue());
//                    System.out.println("Cause: " + removalNotification.getCause().name());   // выводим причину удаления записи.
//                }
//            })
            .weigher(new Weigher<String, Integer>() {   // взвешиваем общий размер записей.
                @Override
                public int weigh(String key, Integer value) {
                    return key.length() + 4;
                }
            })
            .maximumWeight(27)   // максимальный размер кеша в байтах.
            .expireAfterAccess(5, TimeUnit.SECONDS)   // запись удалится спустя 5 секунд с последнего обращения.
            .build();   // кеш готов.

    // Сокращаем использование памяти, сохраняем значения в кеш cache и алгоритм работает.
    /*
        Метод на вход получает индекс числа фибоначчи n.

            Метод сохраняет в кеш cache числа фибоначчи по следующему принципу:
                cache.put("n-2", 0);
                cache.put("n-1", 1);
                cache.put("n  ", 1);
            Элемент по ключу "n  " хранит чсило фибоначчи.

        Метод возврвщвет число фибоначчии по индексу n.
     */
    public static int fibWhileArrayLength3cache(int n) {
        if (n <= 1) return n;
        cache.put("n-2", 0);
        cache.put("n-1", 1);
        cache.put("n  ", 1);
        for (int i = 2; i < n + 1; i++) {
            // сохраняем значение фибоначчи в кеш по ключу "n  ".
            cache.asMap().replace("n  ", cache.asMap().get("n  "), (cache.asMap().get("n-1") + cache.asMap().get("n-2")));
            if (i < n) {
                // продвигаемся дальше по ряду чисел фибоначчи.
                // значение числа по ключу "n-2" меняем на значение числа по ключу "n-1"
                cache.asMap().replace("n-2", cache.asMap().get("n-2"), cache.asMap().get("n-1"));
                // значение числа по ключу "n-1" меняем на значение числа по ключу "n  "
                cache.asMap().replace("n-1", cache.asMap().get("n-1"), cache.asMap().get("n  "));
            }
        }
        return cache.asMap().get("n  ");
    }
}