package ru.otus.homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class BanknoteCell {
    private final Deque<Banknote> banknotes = new ArrayDeque<>();
    private final Banknote.Nominal nominal;
    private int rest = 0;
    private int count = 0;

    public BanknoteCell(Banknote.Nominal nominal) {
        this.nominal = nominal;
    }

    public void pushBanknote(Banknote banknote) {
        banknotes.push(banknote);
        count++;
        rest += banknote.getNominal().nominal();
        System.out.println("В ячейку для номинала " + nominal.nominal() + " добавлена купюра номиналом " +
                banknote.getNominal().nominal());
    }

    public Banknote popBanknote() {
        Banknote result = banknotes.pop();
        rest -= result.getNominal().nominal();
        count--;
        System.out.println("Из ячейки для номинала " + nominal.nominal() + " выдана купюра номиналом " +
                result.getNominal().nominal());
        return result;
    }

    public int getRest() {
        return rest;
    }

    public int getCount() {
        return count;
    }

    public Banknote.Nominal getNominal() {
        return nominal;
    }

}
