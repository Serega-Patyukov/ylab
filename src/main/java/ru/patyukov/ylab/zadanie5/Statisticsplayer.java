package ru.patyukov.ylab.zadanie5;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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
        Path file = Path.of("src/main/resources/static/file/zadanie4/statisticsplayer.txt");

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
                    if (buf.startsWith(("| " + namePlayerWon))) {

                        char[] stringSplit = buf.toCharArray();   // Получаем массив из прочитанной строки.
                                                                 // С 0 элемента - имя.
                                                                // С 10 элемента - количество попед.
                                                               // С 23 - количество поражений.
                        // Увеличиваем на 1 количество побед.
                        String temp = String.valueOf(stringSplit[33]);
                        for (int i = 34; i < 43; i++) {
                            if (stringSplit[i] == ' ') {

                                int number = Integer.parseInt(temp);
                                number++;
                                temp = String.valueOf(number);

                                for (int j = 33; (j < temp.length() + 33) && (j < 43); j++) {
                                    stringSplit[j] = temp.charAt(j - 33);
                                }

                                break;
                            }
                            temp += stringSplit[i];
                        }
                        buf = String.valueOf(stringSplit);   // Сохраняем изменения в строку.
                        stringWriter.write(buf + "\n");   // Сохраняем строку в буффер.
                        flagNamePlayerWon = false;   // Переключаем флаг.
                    }
                    // Если строка в файле начинается с имени проигравшего игрока, то мы эту строку редактируем.
                    else if (buf.startsWith(("| " + namePlayerLost))) {

                        char[] stringSplit = buf.toCharArray();   // Получаем массив из прочитанной строки.
                                                                 // С 0 элемента - имя.
                                                                // С 10 элемента - количество попед.
                                                               // С 23 - количество поражений.

                        // Увеличиваем на 1 количество поражений.
                        String temp = String.valueOf(stringSplit[46]);
                        for (int i = 47; i < 56; i++) {
                            if (stringSplit[i] == ' ') {

                                int number = Integer.parseInt(temp);
                                number++;
                                temp = String.valueOf(number);

                                for (int j = 46; (j < temp.length() + 46) && (j < 56); j++) {
                                    stringSplit[j] = temp.charAt(j - 46);
                                }

                                break;
                            }
                            temp += stringSplit[i];
                        }

                        buf = String.valueOf(stringSplit);   // Сохраняем изменения в строку.
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
            if (flagNamePlayerWon) {
                char[] chars = new char[58];

                Arrays.fill(chars, ' ');

                for (int i = 2; (i < namePlayerWon.length() + 2) && (i < 30); i++) {
                    chars[i] = namePlayerWon.charAt(i - 2);
                }
                chars[0] = '|';
                chars[31] = '|';
                chars[33] = '1';
                chars[44] = '|';
                chars[46] = '0';
                chars[56] = '|';
                chars[57] = '\n';

                stringWriter.write(String.valueOf(chars));
            }
            if (flagNamePlayerLost) {
                char[] chars = new char[58];

                Arrays.fill(chars, ' ');

                for (int i = 2; (i < namePlayerLost.length() + 2) && (i < 30); i++) {
                    chars[i] = namePlayerLost.charAt(i - 2);
                }
                chars[0] = '|';
                chars[31] = '|';
                chars[33] = '0';
                chars[44] = '|';
                chars[46] = '1';
                chars[56] = '|';
                chars[57] = '\n';

                stringWriter.write(String.valueOf(chars));
            }

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
        Path file = Path.of("src/main/resources/static/file/zadanie4/statisticsplayer.txt");

        // Проверяем файл на существование.
        if (Files.exists(file)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toFile()))) {
                System.out.println("\tСтатистика игры:\n");
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
