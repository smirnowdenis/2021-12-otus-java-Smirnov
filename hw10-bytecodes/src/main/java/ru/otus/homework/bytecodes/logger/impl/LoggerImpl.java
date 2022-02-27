package ru.otus.homework.bytecodes.logger.impl;

import ru.otus.homework.bytecodes.annotation.Log;
import ru.otus.homework.bytecodes.logger.api.Logger;

public class LoggerImpl implements Logger {
    @Log
    @Override
    public void calculation() {
    }

    @Log
    @Override
    public void calculation(int param1) {
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
    }
}
