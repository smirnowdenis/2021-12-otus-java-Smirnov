package ru.otus.homework.bytecodes.logger.api;

public interface Logger {
    void calculation();

    void calculation(int param1);

    void calculation(int param1, int param2);

    void calculation(int param1, int param2, String param3);
}
