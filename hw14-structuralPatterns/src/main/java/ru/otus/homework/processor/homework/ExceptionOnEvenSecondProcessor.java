package ru.otus.homework.processor.homework;

import ru.otus.homework.model.Message;
import ru.otus.homework.processor.Processor;
import ru.otus.homework.processor.homework.exception.EvenSecondException;
import ru.otus.homework.processor.homework.provider.api.DateTimeProvider;

public class ExceptionOnEvenSecondProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ExceptionOnEvenSecondProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (isEvenSecond()) {
            throw new EvenSecondException();
        }

        return message;
    }

    private boolean isEvenSecond() {
        return dateTimeProvider.getDate().getSecond() % 2 == 0;
    }
}
