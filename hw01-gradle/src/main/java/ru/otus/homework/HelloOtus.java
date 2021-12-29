package ru.otus.homework;

import com.google.common.collect.ImmutableSet;

public class HelloOtus {
    public static void main(String[] args) {
        ImmutableSet<String> colorSet = ImmutableSet.of("red", "blue", "yellow", "orange", "purple");
        colorSet.forEach(System.out::println);
    }
}
