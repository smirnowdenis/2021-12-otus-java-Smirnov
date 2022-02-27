package ru.otus.homework.bytecodes;

import ru.otus.homework.bytecodes.ioc.Ioc;
import ru.otus.homework.bytecodes.logger.api.Logger;
import ru.otus.homework.bytecodes.logger.impl.LoggerImpl;

public class App {
    public static void main(String[] args) {
        Logger logger = Ioc.createLoggerInstance(new LoggerImpl());

        logger.calculation();
        logger.calculation(1);
        logger.calculation(1, 2);
        logger.calculation(1, 2, "param");
    }
}
