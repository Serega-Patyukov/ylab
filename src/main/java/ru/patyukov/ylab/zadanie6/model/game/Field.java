package ru.patyukov.ylab.zadanie6.model.game;

// Квадратное поле.
public class Field {

    private Cell[][] cells;   // Массив клеток.
    private int n;           // Размерность поля.

            // КОНСТРУКТОРЫ

    public Field() {
        maine();
    }

            // МЕТОДЫ

    public boolean saveCellInField(Cell cell, Player player) {

        // Метод сохраняет клетку в поле.
    /*
        На вход метод получает параметры клетку cell и игрока player.
        Если клетку удалось сохранить в поле, то метод возвращает true.
        Иначе false.
     */

        if (cell == null || player == null) return false;

        // Проверяем клетку на поле. Свободна или нет.
        if (cells[cell.getY()][cell.getX()].isStatus()) {
            cells[cell.getY()][cell.getX()].setValue(player.getValue());   // Записываем туда символ.
            cells[cell.getY()][cell.getX()].setNamePlayer(player.getName());   // Указываем имя игрока, который будет владеть этой клеткой.
            cells[cell.getY()][cell.getX()].setStatus(false);   // И делаем клетку недоступной.
            return true;
        }

        return false;

    }    // Метод сохраняет клетку в поле.
    public String gameOverFinish() {

        // Метод считает количество клеток три в ряд.
    /*
        Если такой ряд находится, то метод возвращает имя выигравшего игрока, иначе пустую строку.
        Метод работает с полем 3х3
     */

        String namePlayer = "";

        int result = 1;   // Хранит количество клеток в ряд.

        // Находим количество клеток в ряд по горизонтали.
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length - 1; j++) {
                if (!cells[i][j].isStatus() && !cells[i][j + 1].isStatus()) {
                    if (cells[i][j].getNamePlayer().equals(cells[i][j + 1].getNamePlayer())) {
                        result++;
                        if (result >= 3) return cells[i][j].getNamePlayer();
                    }
                    else result = 1;
                }
                else result = 1;
            }
            result = 1;
        }

        // Находим количество клеток в ряд по вертикали.
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length - 1; j++) {
                if (!cells[j][i].isStatus() && !cells[j + 1][i].isStatus()) {
                    if (cells[j][i].getNamePlayer().equals(cells[j + 1][i].getNamePlayer())) {
                        result++;
                        if (result >= 3) return cells[j][i].getNamePlayer();
                    }
                    else result = 1;
                }
                else result = 1;
            }
            result = 1;
        }

        // Находим количество клеток в ряд по диагонали слева на право вниз.
        for (int i = 0; i < cells.length - 1; i++) {
            if (!cells[i][i].isStatus() && !cells[i + 1][i + 1].isStatus()) {
                if (cells[i][i].getNamePlayer().equals(cells[i + 1][i + 1].getNamePlayer())) {
                    result++;
                    if (result >= 3) return cells[i][i].getNamePlayer();
                }
                else result = 1;
            }
            else result = 1;
        }
        result = 1;

        // Находим количество клеток в ряд по диагонали слева на право вверх.
        for (int i = cells.length - 1, j = 0; i > 0; i--, j++) {
            if (!cells[i][j].isStatus() && !cells[i - 1][j + 1].isStatus()) {
                if (cells[i][j].getNamePlayer().equals(cells[i - 1][j + 1].getNamePlayer())) {
                    result++;
                    if (result >= 3) return cells[i][j].getNamePlayer();
                }
                else result = 1;
            }
            else result = 1;
        }

        return namePlayer;

    }    // Метод считает количество клеток три в ряд.
    public Cell cell(int x, int y) {

        // Метод по координатам возвращает клетку.

        return cells[y][x];

    }   // Метод по координатам возвращает клетку.
    public boolean gameOver() {

        // Метод проверяет есть ли свободные клетки на поле. false - нет. true - есть.

        boolean flag = false;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].isStatus()) return true;
            }
        }

        return flag;

    }       // Метод проверяет есть ли свободные клетки на поле.
    public void printFiled() {

        // Выводим поле в консоль.
    /*
        Метод корректно выводит поле в консоль при условии, что
        размерность поля от 3х3 до 10х10.
     */

        System.out.println();

        for (int i = 0; i <= cells.length + 1; i++) {

            // Линия.
            for (int k = 0; k <= cells.length; k++) {
                if (k == 0) System.out.print("---------");
                else System.out.print("------");
            }
            System.out.println();
            if (i == cells.length + 1) break;

            // Выводим первую строку с координатой х.
            if (i == 0) {
                System.out.print("|  y\\x  |  ");//---------------------// Выводим первую клетку строки.
                for (int j = 0; j < cells[i].length; j++) {//----------// Выводим следующие клетки.
                    System.out.print(j + "  |  ");
                }
            }

            // Выводим следующие строки. Вначале каждой строки указываем координату у.
            else {
                System.out.print("|   " + (i - 1) + "   |  ");//-----------------// Выводим первую клетку строки.
                for (int j = 0; j < cells[i - 1].length; j++) {//---------------// Выводим следующие клетки.
                    System.out.print(cells[i - 1][j].getValue() + "  |  ");
                }
            }
            System.out.println();
        }
    }       // Выводим поле в консоль.
    private void maine() {

        // Задаем значения по умолчанию.

        // Задаем размерность поля.
        /*
            Для корректного отображения поля в консоли методом printFiled(),
            размерность поля должна быть от 3х3 до 10х10.
         */
        setN(3);

        // Заполняем массив клеток клетками (заполняем поле клетками).
        cells  = new Cell[n][n];
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = new Cell(y, x);//---// Задаем координаты клетки.
            }
        }
    }          // Задаем значения по умолчанию.

            // GET SET

    public int setN(int n) {

        // Метод установки размерности поля.
    /*
        Метод на вход получает размерность поля в виде одного числа n.
        Если число соответствует требованиям (n >= 3) && (n <= 10),
        то метод устанавливает размерность поля и возвращает 1.
        Иначе метод не изменяет размерность поля и возвращает -1.
     */

        if ( (n >= 3) && (n <= 10) ) {
            this.n = n;
            return 1;
        }
        else return -1;
    }
    public int getN() {
        return n;
    }

    public Cell[][] getCells() {
        return cells;
    }
    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }
}
