package ru.patyukov.ylab.zadanie2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        cmdDel();   // Очищаем консоль.

        Scanner scanner = new Scanner(System.in);

        Field field = new Field();   // Создаем поле.

        System.out.println("\n\n\t\tКрестики нолики");

        // Создаем игроков.
        // Первый игрок.
        Player player1;
        System.out.print("\nВведите имя первого игрока - ");
        String namePlayer = scanner.nextLine();
        while (true) {
            System.out.print("Введите символ первого игрока (Х или О) - ");
            String namePlayerValue = scanner.nextLine();
            if (namePlayerValue.equals("Х") || namePlayerValue.equals("О")) {
                player1 = new Player(namePlayer, namePlayerValue);   // Создаем первого игрока.
                break;
            }
        }

        // Второй игрок.
        Player player2;
        System.out.print("\nВведите имя второго игрока - ");
        namePlayer = scanner.nextLine();
        if (player1.getValue().equals("О")) player2 = new Player(namePlayer, "Х");   // Создаем первого игрока.
        else player2 = new Player(namePlayer, "О");   // Создаем первого игрока.

        // Определяем кто первым начнет.
        if ( ((int) ((Math.random()) * 10)) < 5 ) player1.setStartStop(true);   // Начьнет первый.
        else player2.setStartStop(true);   // Начьнет второй.

        cmdDel();

        // ИГРА НАЧАЛАСЬ.
        while (true) {
            System.out.println("\n\n\t\tКрестики нолики\n");

            field.printFiled();   // Выводим поле.

                // ПОДВОДИМ ИТОГИ ОЧЕРЕДНОГО ХОДА.
            // Проверяем на ничью.
            if (!field.gameOver()) {
                System.out.println("\nНИЧЬЯ !!!\n");

                    // СТАТИСТИКА.
                // Выводим на экран.
                Statisticsplayer.printStatisticsPlayer();
                return;
            }
            // Проверяем кто выиграл.
            namePlayer = field.gameOverFinish();
            if (!namePlayer.equals("")) {
                System.out.println("\n" + namePlayer + " - ВЫИГРАЛ !!!\n");

                    // СТАТИСТИКА.
                // Сохраняем.
                if (namePlayer.equals(player1.getName())) Statisticsplayer.statisticsPlayer(player1.getName(), player2.getName());
                else Statisticsplayer.statisticsPlayer(player2.getName(), player1.getName());
                // Выводим на экран.
                Statisticsplayer.printStatisticsPlayer();
                return;
            }

            // Первый игрок делат очередной ход.
            if (player1.isStartStop()) {
                Cell cell = Cell.xy(player1, field.getN());
                if (cell != null) {
                    if (field.saveCellInField(cell, player1)) {
                        player1.setStartStop(false);
                        player2.setStartStop(true);
                    }
                }
                else {
                    System.out.println("Входные данные метода public static Cell xy(Player player, int n)");
                    System.out.println("Не соответствуют трнебованиям");
                    return;
                }
            }
            // Второй игрок делат очередной ход.
            else {
                if (player2.isStartStop()) {
                    Cell cell = Cell.xy(player2, field.getN());
                    if (cell != null) {
                        if (field.saveCellInField(cell, player2)) {
                            player1.setStartStop(true);
                            player2.setStartStop(false);
                        }
                    }
                    else {
                        System.out.println("Входные данные метода public static Cell xy(Player player, int n)");
                        System.out.println("Не соответствуют трнебованиям");
                        return;
                    }
                }
            }
        }
    }

    // Метод очищает консоль.
    public static void cmdDel() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
