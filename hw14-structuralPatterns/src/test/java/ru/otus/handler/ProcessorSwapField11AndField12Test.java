package ru.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.handler.ComplexProcessor;
import ru.otus.homework.model.Message;
import ru.otus.homework.processor.homework.ProcessorSwapField11AndField12;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessorSwapField11AndField12Test {

    @Test
    @DisplayName("Тестируем ProcessorSwapField11AndField12")
    void swapProcessorTest() {
        String field11Value = "field11";
        String field12Value = "field12";
        var message = new Message.Builder(1L).field11(field11Value).field12(field12Value).build();


        var complexProcessor = new ComplexProcessor(List.of(new ProcessorSwapField11AndField12()), (ex) -> {
        });

        Message processedMessage = complexProcessor.handle(message);

        assertThat(processedMessage.getField11()).isEqualTo(field12Value);
        assertThat(processedMessage.getField12()).isEqualTo(field11Value);
    }
}
