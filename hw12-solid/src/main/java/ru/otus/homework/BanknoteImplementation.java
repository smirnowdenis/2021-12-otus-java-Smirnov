package ru.otus.homework;

public class BanknoteImplementation implements Banknote {
    private final Banknote.Nominal nominal;

    public BanknoteImplementation(Banknote.Nominal nominal) {
        this.nominal = nominal;
    }

    @Override
    public Banknote.Nominal getNominal() {
        return this.nominal;
    }
}
