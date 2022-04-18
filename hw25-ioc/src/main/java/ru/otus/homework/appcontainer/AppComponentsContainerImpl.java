package ru.otus.homework.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.homework.appcontainer.api.AppComponent;
import ru.otus.homework.appcontainer.api.AppComponentsContainer;
import ru.otus.homework.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.homework.exception.IncorrectConfigException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        List<Class<?>> configClassesAsList = List.of(configClasses);

        configClassesAsList.forEach(this::checkConfigClass);
        processConfigs(configClassesAsList);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.SubTypes, Scanners.TypesAnnotated);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);

        processConfigs(configClasses);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findAny()
                .orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private void processConfigs(Collection<Class<?>> configClasses) {
        configClasses.stream()
                .sorted(Comparator.comparingInt(configClass -> configClass.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        List<Method> methodsWithAppComponentAnnotation = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        if (!methodsWithAppComponentAnnotation.isEmpty()) {
            processConfigMethods(configClass, methodsWithAppComponentAnnotation);
        }
    }

    private void processConfigMethods(Class<?> configClass, List<Method> methodsWithAppComponentAnnotation) {
        try {
            Object config = configClass.getDeclaredConstructor().newInstance();
            methodsWithAppComponentAnnotation
                    .forEach(method -> invokeMethodAndPutComponentToContainer(config, method));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IncorrectConfigException(
                    String.format("Cannot instantiate %s class with default constructor.", configClass.getName()), e);
        }
    }

    private void invokeMethodAndPutComponentToContainer(Object config, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();
            Object[] methodArguments = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                methodArguments[i] = getAppComponent(types[i]);
            }

            Object component = method.invoke(config, methodArguments);
            String methodName = method.getAnnotation(AppComponent.class).name();

            if (appComponentsByName.containsKey(methodName)) {
                throw new IncorrectConfigException("You have duplicate configured beans", new IllegalArgumentException());
            }
            appComponents.add(component);
            appComponentsByName.put(methodName, component);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IncorrectConfigException(
                    String.format("Cannot invoke config %s method  %s", config.getClass().getName(), method.getName()), e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
