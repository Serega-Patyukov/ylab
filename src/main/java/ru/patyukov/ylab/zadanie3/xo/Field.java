package ru.patyukov.ylab.zadanie3.xo;

// Квадратное поле.
public class Field {

    // Размерность поля.
    private int n;

    // Массив клеток.
    private Cell[][] cells;

    // Крнструктор.
    public Field() {
        maine();
    }

    // Задаем значения по умолчанию.
    private void maine() {

        // Задаем размерность поля.
        /*
            Для корректного отображения поля методом printFiled(),
            размерность поля должна быть от 3х3 до 10х10.
         */
        setN(3);

        // Заполняем массив клеток клетками.
        cells  = new Cell[n][n];
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                cells[x][y] = new Cell(y, x);//---// Задаем координаты клетки.
            }
        }
    }

    // Метод считаем количество клеток три в ряд.
    /*
        Метод работает с полем 3х3
        Если такой ряд находится, то метод возвращает имя выигравшего игрока, иначе пусиую строку.
     */
    public String gameOverFinish() {
        String namePlayer = "";

        int result = 1;   // Хранит количество клеток в ряд.

        // Находим количество клеток в ряд по горизонтали.
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length - 1; j++) {
                if (!cells[i][j].isStstus() && !cells[i][j + 1].isStstus()) {
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
                if (!cells[j][i].isStstus() && !cells[j + 1][i].isStstus()) {
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
            if (!cells[i][i].isStstus() && !cells[i + 1][i + 1].isStstus()) {
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
            if (!cells[i][j].isStstus() && !cells[i - 1][j + 1].isStstus()) {
                if (cells[i][j].getNamePlayer().equals(cells[i - 1][j + 1].getNamePlayer())) {
                    result++;
                    if (result >= 3) return cells[i][j].getNamePlayer();
                }
                else result = 1;
            }
            else result = 1;
        }

        return namePlayer;
    }

    // Проверяем есть ли свободные клетки на поле. false - нет. true - есть.
    public boolean gameOver() {
        boolean flag = false;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].isStstus()) return true;
            }
        }

        return flag;
    }

    // Метод сохраняет клетку в поле.
    /*
        На вход метод получает параметры клетку cell и игрока player.
        Если клетку удалось сохранить в поле, то метод возвращает true.
        Иначе false.
     */
    public boolean saveCellInField(Cell cell, Player player) {

        if (cell == null || player == null) return false;

        // Проверяем клетку на поле. Свободна или нет.
        if (cells[cell.getY()][cell.getX()].isStstus()) {
            cells[cell.getY()][cell.getX()].setValue(player.getValue());   // Записывеем туда символ.
            cells[cell.getY()][cell.getX()].setNamePlayer(player.getName());   // Указываем имя игрока, который будет владеть этой клеткой.
            cells[cell.getY()][cell.getX()].setStstus(false);   // И делаем клетку недоступной.
            return true;
        }

        return false;
    }

    // Выводим поле на экран.
    /*
        Метод корректно выводит поле на экран при условии что
        размерность поля от 3х3 до 10х10.
     */
    public void printFiled() {

        System.out.println();

        for (int i = 0; i <= cells.length + 1; i++) {

            // Линия.
            for (int k = 0; k <= cells.length; k++) {
                if (k == 0) System.out.print("---------");
                else System.out.print("------");
            }
            System.out.println();
            if (i == cells.length + 1) break;

            // Выводим первую строку с кординатой х.
            if (i == 0) {
                System.out.print("|  y\\x  |  ");//---------------------// Выводим первую клетку строки.
                for (int j = 0; j < cells[i].length; j++) {//----------// Выводим следующие клетки.
                    System.out.print(j + "  |  ");
                }
            }

            // Выводим следующие строки. Вначале каждой строки указываем кординату у.
            else {
                System.out.print("|   " + (i - 1) + "   |  ");//-----------------// Выводим первую клетку строки.
                for (int j = 0; j < cells[i - 1].length; j++) {//---------------// Выводим следующие клетки.
                    System.out.print(cells[i - 1][j].getValue() + "  |  ");
                }
            }
            System.out.println();
        }
    }

    // Метод установки размерности поля.
    /*
        Метод на вход получает размерность поля в виде одного числа n.
        Если число соответствует требованиям (n >= 3) && (n <= 10),
        то метод устанавливает размерность поля и возвращает 1.
        Иначе метод не изменяет размерность поля и возвращает -1.
     */
    public int setN(int n) {
        if ( (n >= 3) && (n <= 10) ) {
            this.n = n;
            return 1;
        }
        else return -1;
    }

    public int getN() {
        return n;
    }

    // Метод по координатам возврвщвет клетку.
    public Cell getCell(int x, int y) {
        return cells[y][x];
    }
}
