package ru.patyukov.ylab.zadanie6.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.patyukov.ylab.zadanie6.model.ModelStatisticsPlayer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Класс статистики игры.
@Data
@Slf4j
public class StatisticsPlayer {

    private String path = "src/main/resources/static/file/zadanie6/statisticsplayer.txt";   // Относительное имя файла статистики игры.
    private List<ModelStatisticsPlayer> statisticsArrayList = new ArrayList<>();           // Список для работы со статистикой.
    private List<ModelStatisticsPlayer> modelStatisticsPlayer = new ArrayList<>();        // Статистика всех игроков из БД.

            // МЕТОДЫ

    public int saveStatisticsPlayer(String namePlayerWon, String namePlayerLost) {

        // Метод сохраняет статистику игры.
    /*
        Метод на вход получает имена:
            namePlayerWon - выигравшего игрока;
            namePlayerLost - проигравшего игрока.

            Если в работе метода возникнет ошибка, то метод вернет -1.
            Иначе 1.
     */

        Path file = Path.of(path);   // Файл для хранения статистики игры.

        boolean flagNamePlayerWon = true;    // Флаг. true - имя выигравшего игрока в файле не найдено.
                                                  // false - имя выигравшего игрока в файле найдено.
        boolean flagNamePlayerLost = true;   // Флаг. true - имя проигравшего игрока в файле не найдено.
                                                  // false - имя проигравшего игрока в файле найдено.

        // Проверяем файл на существование.
        if (Files.exists(file)) {

            // Буфер для хранения считанной информации из файла.
            StringWriter stringWriter = new StringWriter();

            // Считываем и редактируем содержимое файла в буфер stringWriter.
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toFile()))) {
                while (bufferedReader.ready()) {
                    // Получаем очередную строку из файла статистики игры.
                    String buf = bufferedReader.readLine();
                    // Если строка в файле начинается с имени выигравшего игрока, то мы эту строку редактируем.
                    if (buf.startsWith(("| " + namePlayerWon))) {

                        char[] stringSplit = buf.toCharArray();   // Получаем массив из прочитанной строки.
                                                                 // С 2 по 30 элемента - имя.
                                                                // С 33 по 43 элемента - количество побед.
                                                               // С 46 по 56 элемента - количество поражений.
                                                              // Если захочешь изменить эти параметры,
                                                             // то измени их и в методе printStatisticsPlayer().
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
                        stringWriter.write(buf + "\n");   // Сохраняем строку в буфер.
                        flagNamePlayerWon = false;   // Переключаем флаг.
                    }
                    // Если строка в файле начинается с имени проигравшего игрока, то мы эту строку редактируем.
                    else if (buf.startsWith(("| " + namePlayerLost))) {

                        char[] stringSplit = buf.toCharArray();   // Получаем массив из прочитанной строки.
                                                                 // С 2 по 30 элемента - имя.
                                                                // С 33 по 43 элемента - количество побед.
                                                               // С 46 по 56 элемента - количество поражений.
                                                              // Если захочешь изменить эти параметры,
                                                             // то измени их и в методе printStatisticsPlayer().
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
                        stringWriter.write(buf + "\n");   // Сохраняем строку в буфер.
                        flagNamePlayerLost = false;   // Переключаем флаг.
                    }
                    else stringWriter.write(buf + "\n");   // Сохраняем строку в буфер.
                }
            } catch (IOException e) {
                System.out.println("\nОШИБКА - не удалось сохранить статистику игры (процесс чтения файла)\n" +
                        "метод statisticsPlayer() класса StatisticsPlayer\n");
                e.printStackTrace();
                return -1;
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

            // Сохраняем буфер stringWriter в файл статистики игры.
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.toFile()))) {
                bufferedWriter.write(stringWriter.toString());
            } catch (IOException e) {
                System.out.println("\nОШИБКА - не удалось сохранить статистику игры (процесс записи файла)\n" +
                        "метод statisticsPlayer() класса StatisticsPlayer\n");
                e.printStackTrace();
                return -1;
            }

        }
        else {
            System.out.println("\nОШИБКА - файл для сохранения статистики игры не найден\n" +
                    "метод statisticsPlayer() класса StatisticsPlayer\n");
            return -1;
        }



        return 1;

    }   // Метод сохраняет статистику игры.
    public int printStatisticsPlayer(boolean flag) {

        // Метод выводит статистику игры в консоль или сохраняет в лис.
        /*
            flag = true - метод выводит статистику в консоль.
            flag = false - метод сохраняет статистику в лист.
            Если в работе метода возникнет ошибка, то метод вернет -1.
            Иначе 1.
         */

        Path file = Path.of(path);   // Файл для хранения статистики.

        // Проверяем файл на существование.
        if (Files.exists(file)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toFile()))) {
                for (int j = 0; bufferedReader.ready(); j++) {
                    String buf = bufferedReader.readLine();
                    if (flag) System.out.println(buf);   // Вводим статистику в консоль.
                    else {   // Сохраняем статистику в лист.
                        /*
                            В полученной строке согласно работе метода saveStatisticsPlayer()
                                - С 2 по 30 символа - имя.
                                - С 33 по 43 символа - количество побед.
                                - С 46 по 56 символа - количество поражений.
                                А после имени и количества побед и поражений стоит символ пробел ' '
                         */
                        String name = "";   // Имя игрока в статистике.
                        String won = "";   // Количество побед.
                        String lost = ""; // Количество поражений.

                        if (j > 2) {   // Пропускаем первые 3 строчки в файле.
                            for (int i = 2; i < 30; i++) {
                                if (buf.charAt(i) == ' ') break;
                                name += buf.charAt(i);   // Получаем имя.
                            }
                            for (int i = 33; i < 43; i++) {
                                if (buf.charAt(i) == ' ') break;
                                won += buf.charAt(i);   // Получаем количество побед.
                            }
                            for (int i = 46; i < 56; i++) {
                                if (buf.charAt(i) == ' ') break;
                                lost += buf.charAt(i);   // Получаем количество поражений.
                            }
                            statisticsArrayList.add(new ModelStatisticsPlayer(name, Integer.parseInt(won), Integer.parseInt(lost)));
                        }
                    }
                }
            } catch (IOException e) {
                log.warn("ОШИБКА - не удалось вывести статистику игры. Метод printStatisticsPlayer() класса StatisticsPlayer");
                return -1;
            }
        }
        else {
            log.warn("ОШИБКА - файл статистики не найден. Метод printStatisticsPlayer() класса StatisticsPlayer");
            return -1;
        }
        return 1;

    }   // Метод выводит в консоль статистику игры.
}
