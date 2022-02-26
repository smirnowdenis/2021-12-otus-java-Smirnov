package ru.otus.homework;

import java.util.ArrayList;
import java.util.List;

public interface ATM {
    void putBanknote(Banknote banknote);

    void putBanknotes(ArrayList<Banknote> banknotes);

    void printBalance();

    int getBalance();

    List<Banknote> getSum(long sum);
}
