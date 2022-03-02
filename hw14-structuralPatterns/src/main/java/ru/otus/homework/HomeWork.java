package ru.otus.homework;


import ru.otus.homework.handler.ComplexProcessor;
import ru.otus.homework.listener.ListenerPrinterConsole;
import ru.otus.homework.listener.homework.HistoryListener;
import ru.otus.homework.model.Message;
import ru.otus.homework.processor.homework.ExceptionOnEvenSecondProcessor;
import ru.otus.homework.processor.homework.ProcessorSwapField11AndField12;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {

    public static void main(String[] args) {
        var processors = List.of(new ProcessorSwapField11AndField12(),
                new ExceptionOnEvenSecondProcessor(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, System.out::println);
        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();

        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);

        System.out.println(historyListener.findMessageById(1L));
    }
}
