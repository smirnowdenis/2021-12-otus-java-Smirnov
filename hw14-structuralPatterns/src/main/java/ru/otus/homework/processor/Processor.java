package ru.otus.homework.processor;

import ru.otus.homework.model.Message;

public interface Processor {

    Message process(Message message);
}
