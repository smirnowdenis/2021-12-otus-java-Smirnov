package ru.otus.homework.exception;

public class IncorrectConfigException extends RuntimeException {

    public IncorrectConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectConfigException(Throwable cause) {
        super(cause);
    }
}
