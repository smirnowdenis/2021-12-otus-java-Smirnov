package ru.otus.homework;

public class Counter {
    private int value = 1;
    private Direction direction = Direction.INCREASE;

    public int getNextValue() {
        calculateDirection();
        return direction.equals(Direction.INCREASE) ? value++ : value--;
    }

    private void calculateDirection() {
        if (value == 10) {
            direction = Direction.DECREASE;
        } else if (value == 1) {
            direction = Direction.INCREASE;
        }
    }
}
