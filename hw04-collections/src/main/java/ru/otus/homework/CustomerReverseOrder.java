package ru.otus.homework;


import java.util.ArrayDeque;

public class CustomerReverseOrder {
    private final ArrayDeque<Customer> deque = new ArrayDeque<>();

    //todo: 2. надо реализовать методы этого класса

    public void add(Customer customer) {
        deque.addLast(customer);
    }

    public Object take() {
        return deque.pollLast();
    }
}