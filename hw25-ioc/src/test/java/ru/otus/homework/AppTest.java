package ru.otus.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.homework.appcontainer.AppComponentsContainerImpl;
import ru.otus.homework.config.AppConfig;
import ru.otus.homework.services.EquationPreparer;
import ru.otus.homework.services.IOService;
import ru.otus.homework.services.PlayerService;

import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(value = {"GameProcessor, ru.otus.homework.services.GameProcessor",
            "GameProcessorImpl, ru.otus.homework.services.GameProcessor",
            "gameProcessor, ru.otus.homework.services.GameProcessor",

            "IOService, ru.otus.homework.services.IOService",
            "IOServiceConsole, ru.otus.homework.services.IOService",
            "ioService, ru.otus.homework.services.IOService",

            "PlayerService, ru.otus.homework.services.PlayerService",
            "PlayerServiceImpl, ru.otus.homework.services.PlayerService",
            "playerService, ru.otus.homework.services.PlayerService",

            "EquationPreparer, ru.otus.homework.services.EquationPreparer",
            "EquationPreparerImpl, ru.otus.homework.services.EquationPreparer",
            "equationPreparer, ru.otus.homework.services.EquationPreparer"
    })
    public void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass) throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.otus.homework.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        var fields = Arrays.stream(component.getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toList());

        for (var field: fields){
            var fieldValue = field.get(component);
            assertThat(fieldValue).isNotNull().isInstanceOfAny(IOService.class, PlayerService.class,
                    EquationPreparer.class, PrintStream.class, Scanner.class);
        }

    }
}