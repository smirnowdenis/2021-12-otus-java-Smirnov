package ru.otus.homework.processor.homework.provider.api;

import java.time.LocalDateTime;

@FunctionalInterface
public interface DateTimeProvider {
    LocalDateTime getDate();
}
