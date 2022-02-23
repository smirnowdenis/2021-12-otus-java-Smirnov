package ru.otus.homework;

import java.util.ArrayList;

public interface ATM {
    void putBanknote(Banknote banknote);

    void putBanknotes(ArrayList<Banknote> banknotes);

    void printBalance();

    int getBalance();

    ArrayList<Banknote> getSum(long sum);
}
