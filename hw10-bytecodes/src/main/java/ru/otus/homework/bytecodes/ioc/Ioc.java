package ru.otus.homework.bytecodes.ioc;

import ru.otus.homework.bytecodes.logger.api.Logger;
import ru.otus.homework.bytecodes.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class Ioc {

    private Ioc() {
        throw new UnsupportedOperationException();
    }

    public static Logger createLoggerInstance(Logger logger) {
        InvocationHandler handler = new LoggerInvocationHandler(logger);
        return (Logger) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{Logger.class}, handler);
    }

    private static class LoggerInvocationHandler implements InvocationHandler {
        private final Logger logger;
        private final Set<String> logMethodNamesWithParamTypes;

        LoggerInvocationHandler(Logger logger) {
            this.logger = logger;
            logMethodNamesWithParamTypes = ReflectionUtils.getLogMethodNamesWithParamTypes(logger);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws InvocationTargetException, IllegalAccessException {
            if (logMethodNamesWithParamTypes.contains(ReflectionUtils.getMethodNameWithParamTypes(method))) {
                logMethodNameAndParams(method, args);
            }
            return method.invoke(logger, args);
        }

        private void logMethodNameAndParams(Method method, Object[] args) {
            String params = Optional.ofNullable(args)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            if(params.length() == 0){
                System.out.printf("executed method: %s without params%n", method.getName());
            } else {
                System.out.printf("executed method: %s with params: %s%n", method.getName(), params);
            }
        }
    }
}
