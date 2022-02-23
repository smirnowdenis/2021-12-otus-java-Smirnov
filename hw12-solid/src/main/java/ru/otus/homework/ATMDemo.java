package ru.otus.homework;

import java.util.ArrayList;

public class ATMDemo {
    public static void main(String[] args) {
        ATM atm = new ATMImplementation();

        ArrayList<Banknote> banknotes = new ArrayList<>();
        banknotes.add(new BanknoteImplementation(Banknote.Nominal.TEN));
        banknotes.add(new BanknoteImplementation(Banknote.Nominal.TEN));
        banknotes.add(new BanknoteImplementation(Banknote.Nominal.ONE_HUNDRED));
        banknotes.add(new BanknoteImplementation(Banknote.Nominal.ONE_THOUSAND));
        banknotes.add(new BanknoteImplementation(Banknote.Nominal.FIVE_HUNDRED));
        banknotes.add(new BanknoteImplementation(Banknote.Nominal.FIFTY));
        banknotes.add(new BanknoteImplementation(Banknote.Nominal.FIFTY));
        atm.putBanknotes(banknotes);

        atm.printBalance();

        try {
            atm.getSum(1000000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            atm.getSum(1660);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        atm.printBalance();

        atm.putBanknotes(banknotes);

        atm.printBalance();
    }
}
