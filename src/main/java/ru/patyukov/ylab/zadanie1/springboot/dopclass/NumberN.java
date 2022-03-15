package ru.patyukov.ylab.zadanie1.springboot.dopclass;

import java.util.Arrays;

public class NumberN {
    private int n;
    private int[] memor;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getMemor(int n) {
        memor = new int[n + 1];
        Arrays.fill(memor, -1);
        return memor;
    }
}
