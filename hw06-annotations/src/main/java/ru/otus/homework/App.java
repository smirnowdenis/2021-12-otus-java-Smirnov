package ru.otus.homework;

import java.lang.reflect.Method;

public class App {
    public static void main(String[] args) {
        try {
            Class<Runner> clazz = Runner.class;
            Method run = clazz.getDeclaredMethod("run", Class.class);
            run.setAccessible(true);

            run.invoke(new Runner(), TestClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
