package ru.patyukov.ylab.zadanie2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

// Класс статистики игры.
public class Statisticsplayer {

    // Метод сохраняет статистику игры.
    /*
        Метод на вход получает имена:
            namePlayerWon - выигравшего игрока;
            namePlayerLost - проигравшего игрока.
     */
    public static void statisticsPlayer(String namePlayerWon, String namePlayerLost) {

        // Файл для хранения статистики игры.
        Path file = Path.of("src/main/resources/static/file/zadanie2/statisticsplayer.txt");

        boolean flagNamePlayerWon = true;    // Флаг. true - имя выигравшего игрока в файле не наденно.
                                                  // false - имя выигравшего игрока в файле найденно.
        boolean flagNamePlayerLost = true;   // Флаг. true - имя проигравшего игрока в файле не наденно.
                                                  // false - имя проигравшего игрока в файле найденно.

        // Проверяем файл на существование.
        if (Files.exists(file)) {

            // Буффер для хранения считанной информации из файла.
            StringWriter stringWriter = new StringWriter();

            // Считываем и редактируем содержимое файла в буффер stringWriter.
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toFile()))) {
                while (bufferedReader.ready()) {
                    // Получаем очередную строку из файла статистики игры.
                    String buf = bufferedReader.readLine();
                    // Если строка в файле начинается с имени выигравшего игрока, то мы эту строку редактируем.
                    if (buf.startsWith(namePlayerWon)) {
                        String[] stringSplit = buf.split("_");   // Получаем массви из 3 элементов. 0 - имя. 1 - количество попед. 2 - количество поражений.
                        stringSplit[1] = String.valueOf((Integer.parseInt(stringSplit[1]) + 1));   // Увеличиваем на 1 количество побед.
                        buf = stringSplit[0] + "_" + stringSplit[1] + "_" + stringSplit[2];   // Сохраняем изменения в строку.
                        stringWriter.write(buf + "\n");   // Сохраняем строку в буффер.
                        flagNamePlayerWon = false;   // Переключаем флаг.
                    }
                    // Если строка в файле начинается с имени проигравшего игрока, то мы эту строку редактируем.
                    else if (buf.startsWith(namePlayerLost)) {
                        String[] stringSplit = buf.split("_");   // Получаем массви из 3 элементов. 0 - имя. 1 - количество попед. 2 - количество поражений.
                        stringSplit[2] = String.valueOf((Integer.parseInt(stringSplit[2]) + 1));   // Увеличиваем на 1 количество поражений.
                        buf = stringSplit[0] + "_" + stringSplit[1] + "_" + stringSplit[2];   // Сохраняем изменения в строку.
                        stringWriter.write(buf + "\n");   // Сохраняем строку в буффер.
                        flagNamePlayerLost = false;   // Переключаем флаг.
                    }
                    else stringWriter.write(buf + "\n");   // Сохраняем строку в буффер.
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Файл для сохранения статистики не удалось прочитать (процесс чтения файла статистики игры).");
                System.out.println("Не удалось сохранить статистику игры.");
                return;
            }
            if (flagNamePlayerWon) stringWriter.write(namePlayerWon+ "_1_0" + "\n");
            if (flagNamePlayerLost) stringWriter.write(namePlayerLost+ "_0_1" + "\n");

            // Сохраняем буффер stringWriter в файл статистики игры.
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.toFile()))) {
                bufferedWriter.write(stringWriter.toString());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Файл для сохранения статистики не удалось прочитать (процесс записи файла статистики игры).");
                System.out.println("Не удалось сохранить статистику игры");
            }

        }
        else {
            System.out.println("Файл для сохранения статистики игры не найден.");
            System.out.println("Не удалось сохранить статистику игры.");
        }
    }

    // Метод выводит на экран статистику игры.
    public static void printStatisticsPlayer() {

        // Файл для хранения статистики.
        Path file = Path.of("src/main/resources/static/file/zadanie2/statisticsplayer.txt");

        // Проверяем файл на существование.
        if (Files.exists(file)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toFile()))) {
                System.out.println("\tСтатистика игры:\n");
                System.out.println("игрок_выиграл_проиграл");
                while (bufferedReader.ready()) {
                    System.out.println(bufferedReader.readLine());   // Вводим статистику на экран.
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Файл для сохранения статистики не удалось прочитать (процесс вывода статистики на экран).");
                System.out.println("Не удалось вывести на экран статистику игры.");
            }
        }
        else {
            System.out.println("Файл статистики не найден.");
            System.out.println("Не удалось вывести на экран статистику игры.");
        }
    }
}
