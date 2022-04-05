package ru.patyukov.ylab.zadanie6.console;

import ru.patyukov.ylab.zadanie6.services.GameXO;

import java.util.Scanner;

// Консольная игра в крестики нолики.
public class TestGame {

    private static Scanner scanner = new Scanner(System.in);

    private static boolean flag = true;

    public static void main(String[] args) {

        GameXO gameXO = new GameXO();      // Движок игры.

        while (flag) {
            System.out.println("\n\n\t\tКРЕСТИКИ НОЛИКИ\n");

            gameResult(gameXO);   // Метод просмотра истории и управления.
            if (!flag) break;

            gameXO = new GameXO();      // Движок игры. Нужно обнулится.
            System.out.println("=========================================================\n\n");

            createPlayer(gameXO);   // Создаем игроков.
            gameXO.queue();        // Определяем кто первым начнет.

            // ИГРА НАЧАЛАСЬ.
            for (int i = 1; i > 0; i++) {    // Условие i > 0 написано с осознанием полной ответственности за результат работы бесконечного цикла !!!
                                            // i - количество ходов. На последнем ходе будет break.
                gameXO.getField().printFiled();   // Выводим поле.

                // ПОДВОДИМ ИТОГИ ОЧЕРЕДНОГО ХОДА.
                String namePlayer = gameXO.getField().gameOverFinish();   // Получаем имя победителя, если такой есть. Иначе пустую строку.

                // Обрабатываем победителя, если он есть.
                if (!namePlayer.equals("")) {
                    System.out.println("\n" + namePlayer + " - ВЫИГРАЛ !!!");
                    System.out.println("=========================================================\n\n");
                    gameXO.finish(namePlayer);   // Обрабатываем победителя.
                    break;
                }
                // Проверяем на ничью.
                if (!gameXO.getField().gameOver()) {
                    System.out.println("\nНИЧЬЯ !!!");
                    System.out.println("=========================================================\n\n");
                    gameXO.draw();   // Обрабатываем ничью.
                    break;
                }

                if (gameXO.getGameplay().getPlayer1().isStartStop()) goPlayer1(gameXO, i);   // Первый игрок делает очередной ход.
                else if (gameXO.getGameplay().getPlayer2().isStartStop()) goPlayer2(gameXO, i);   // Второй игрок делает очередной ход.
            }
        }
        System.out.println("\nКОНЕЦ !!!");
    }

    public static void goPlayer2(GameXO gameXO, int i) {

        // Второй игрок делает очередной ход.
        /*
            int i - номер хода.
         */

        while (true) {

            // Вводимые игроком координаты клетки.
            int x = -1;
            int y = -1;

            try {
                System.out.println("\tИгрок - " + gameXO.getGameplay().getPlayer2().getName());
                System.out.print("Введите х - ");
                x = Integer.parseInt(scanner.nextLine());
                System.out.print("Введите у - ");
                y = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ОШИБКА - не правильный ввод\n");
                continue;
            }

            if (gameXO.goPlayer2(i, x ,y) != 1) System.out.println("ОШИБКА - не правильный ввод\n");
            else break;
        }

    }    // Второй игрок делает очередной ход.
    public static void goPlayer1(GameXO gameXO, int i) {

        // Первый игрок делает очередной ход.
        /*
            int i - номер хода.
         */

        while (true) {

            // Вводимые игроком координаты клетки.
            int x = -1;
            int y = -1;

            try {
                System.out.println("\tИгрок - " + gameXO.getGameplay().getPlayer1().getName());
                System.out.print("Введите х - ");
                x = Integer.parseInt(scanner.nextLine());
                System.out.print("Введите у - ");
                y = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ОШИБКА - не правильный ввод\n");
                continue;
            }

            if (gameXO.goPlayer1(i, x ,y) != 1) System.out.println("ОШИБКА - не правильный ввод\n");
            else break;
        }

    }   // Первый игрок делает очередной ход.
    public static void createPlayer(GameXO gameXO) {

        // Создаем игроков.

        while (true) {

            // Создаем первого игрока.
            System.out.println("\tВведите имя первого игрока");
            System.out.println("минимум 3 символа\n");
            System.out.print("Имя - ");
            String namePlayer1 = scanner.nextLine();
            System.out.println("\n\tВведите символ первого игрока (Х или О)\n");
            System.out.print("Символ - ");
            String value1 = scanner.nextLine();

            // Создаем второго игрока.
            System.out.println("\n\tВведите имя второго игрока");
            System.out.println("минимум 3 символа\n");
            System.out.print("Имя - ");
            String namePlayer2 = scanner.nextLine();
            System.out.println("\n\tВведите символ второго игрока (Х или О)\n");
            System.out.print("Символ - ");
            String value2 = scanner.nextLine();

//            if (gameXO.createPlayer(namePlayer1, value1, namePlayer2, value2) != 1) System.out.println("ОШИБКА - не правильный ввод\n");
//            else break;
            // Закомментировано потому что изменилась работа метода createPlayer()
            break;
        }
    }      // Создаем игроков.
    public static void gameResult(GameXO gameXO) {

        // Метод просмотра истории и управления.

        // Получаем список имен файлов с историей игр, без директории и расширения.
        if (gameXO.createGameList() != 1) System.out.println("Не удалось получить список имен файлов с историей игр, без директории и расширения.");

        // Цикл управления вывода истории.
        for (int j = 1; j > 0; j++) {    // Условие j > 0 написано с осознанием полной ответственности за результат работы бесконечного цикла !!!

            System.out.println("\tИстория игр предыдущих игроков:\n");

            // Выводим список имен файлов с историей игр.
            for (String buf : gameXO.getListPath()) System.out.println(buf);
            System.out.println();

            System.out.println("\tДЛЯ ЗАПУСКА ИГРЫ ВВЕДИТЕ NEXT\n");
            System.out.println("\tДля просмотра истории игры введите имя из списка");
            System.out.println("\tДля просмотра статистики игры введите STAT");
            System.out.println("\tДля выхода введите EXIT\n");
            System.out.print("Введите - ");

            String buffer = scanner.nextLine();   // Ввод от пользователя.

            // Обработка ввода.
            if (buffer.equals("EXIT")) {
                flag = false;
                return;
            }
            else if (buffer.equals("STAT")) {
                System.out.println();
                System.out.println("\tСтатистика игры:\n");
                if (gameXO.getStatisticsPlayer().printStatisticsPlayer(true) != 1) System.out.println("Не удалось вывести статистику игры");
                System.out.println("*********************************************************");
                System.out.println("=========================================================\n\n");
            } else if (buffer.equals("NEXT"))  return;
            else {
                System.out.println();

                if (gameXO.getListPath().size() == 0) {
                    System.out.println("\tВведенное имя не найдено. ПОВТОРИТЕ !!!");
                    System.out.println("=========================================================\n\n");
                    continue;
                }

                // Ищем введенное имя в списке имен файлов с историей игр, без директории и расширения.
                for (int i = 0; i < gameXO.getListPath().size(); i++) {
                    if (buffer.equals(gameXO.getListPath().get(i))) {
                        try {
                            System.out.println("\tИстория игры - " + buffer);

                            if (buffer.endsWith(".xml")) {
                                System.out.println("\nИстория игры из файла xml");
                                gameXO.setParser(gameXO.getXmlDomParser());
                                gameXO.setGameplay(gameXO.getParser().read(gameXO.getPath() + buffer, null));
                                gameXO.getGameplay().printGameplay();
                            }

                            if (buffer.endsWith(".json")) {
                                System.out.println("\nИстория игры из файла json");
                                gameXO.setParser(gameXO.getJsonSimpleParser());
                                gameXO.setGameplay(gameXO.getParser().read(gameXO.getPath() + buffer, null));
                                gameXO.getGameplay().printGameplay();
                            }

                        } catch (Exception e) {
                            System.out.println("Не удалось посмотреть историю игры предыдущих игроков");
                            System.out.println("Ошибка при загрузке файла с историей игры.");
                            e.printStackTrace();
                        }
                        break;
                    }
                    if (i == gameXO.getListPath().size() - 1) {
                        System.out.println("\tВведенное имя не найдено. ПОВТОРИТЕ !!!");
                        System.out.println("=========================================================\n\n");
                    }
                }
            }
        }
    }       // Метод просмотра истории и управления.
}
