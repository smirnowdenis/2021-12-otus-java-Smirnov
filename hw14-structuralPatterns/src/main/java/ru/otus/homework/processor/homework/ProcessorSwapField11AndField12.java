package ru.otus.homework.processor.homework;


import ru.otus.homework.model.Message;
import ru.otus.homework.processor.Processor;

public class ProcessorSwapField11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field11(message.getField12())
                .field12(message.getField11())
                .build();
    }
}
