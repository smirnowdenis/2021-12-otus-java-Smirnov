package ru.otus.homework;

import java.util.*;

public class ATMImplementation implements ATM {

    private final Map<Banknote.Nominal, BanknoteCell> cells = new HashMap<>();

    public ATMImplementation() {
        EnumSet.allOf(Banknote.Nominal.class)
                .forEach(nominal -> cells.put(nominal, new BanknoteCell(nominal)));
    }

    @Override
    public void putBanknote(Banknote banknote) {
        cells.get(banknote.getNominal()).pushBanknote(banknote);
    }

    @Override
    public void putBanknotes(ArrayList<Banknote> banknotes) {
        banknotes.forEach(this::putBanknote);
    }

    @Override
    public void printBalance() {
        long rest = getBalance();
        System.out.println("Баланс: " + rest);
        EnumSet.allOf(Banknote.Nominal.class)
                .forEach(nominal -> System.out.println("Количество купюр номиналом " + nominal.nominal() + ": " + cells.get(nominal).getCount()));
    }

    @Override
    public int getBalance() {
        return cells.values().stream().mapToInt(BanknoteCell::getRest).sum();
    }

    @Override
    public ArrayList<Banknote> getSum(long sum) {
        System.out.println("Запрос на получение суммы: " + sum);
        if (sum > getBalance()) {
            throw new RuntimeException("Сумму " + sum + " выдать нельзя");
        } else {
            ArrayList<Banknote> outBanknotes = new ArrayList<>();
            List<Banknote.Nominal> toSort = new ArrayList<>(EnumSet.allOf(Banknote.Nominal.class));
            toSort.sort(Comparator.comparing(Banknote.Nominal::nominal).reversed());
            for (Banknote.Nominal nominal : toSort) {
                while (sum % cells.get(nominal).getNominal().nominal() < sum && cells.get(nominal).getRest() > 0) {
                    Banknote banknote = cells.get(nominal).popBanknote();
                    sum -= banknote.getNominal().nominal();
                    outBanknotes.add(banknote);
                }
            }
            return outBanknotes;
        }
    }
}
