package ru.otus.homework;

public interface Banknote {
    enum Nominal {
        TEN(10),
        FIFTY(50),
        ONE_HUNDRED(100),
        FIVE_HUNDRED(500),
        ONE_THOUSAND(1000);

        private final int nominal;

        Nominal(int nominal) {
            this.nominal = nominal;
        }

        public int nominal() {
            return nominal;
        }
    }

    Nominal getNominal();
}
