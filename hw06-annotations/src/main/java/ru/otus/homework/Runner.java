package ru.otus.homework;

import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Runner {

    private void run(Class<?> testClass) {
        System.out.println("----- Running the test from " + testClass + "\n");
        try {
            Method[] allMethods = testClass.getMethods();

            Constructor<?> constructor = testClass.getConstructor();

            var testCount = 0;
            var failedTestCount = 0;

            Method[] setUpMethods = getMethodsWithAnnotation(allMethods, Before.class);
            Method[] testMethods = getMethodsWithAnnotation(allMethods, Test.class);
            Method[] tearDownMethods = getMethodsWithAnnotation(allMethods, After.class);

            for (Method method : testMethods) {
                boolean isMethodFail = false;
                testCount++;
                Object object = constructor.newInstance();
                try {
                    runHelpMethods(object, setUpMethods);
                    System.out.println("   Testing method: " + method.getName());
                    method.invoke(object);
                } catch (Exception e) {
                    isMethodFail = true;
                    System.out.println("*** Test failed, method: " + method.getName() +
                            "\n*** Fail exception: " + e + "\n");
                }

                try {
                    runHelpMethods(object, tearDownMethods);
                } catch (Exception e) {
                    isMethodFail = true;
                    System.out.println("Failed when executing methods with annotation After");
                }

                if (isMethodFail) {
                    failedTestCount++;
                }
            }

            System.out.println("\nTests count is " + testCount +
                    "\nCount of successful tests is " + (testCount - failedTestCount) +
                    "\nCount of failed tests is " + failedTestCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runHelpMethods(Object object, Method[] methods) throws Exception {
        for (Method method : methods) {
            System.out.println("*** Running the methods annotated " + getAnnotationName(method));
            method.invoke(object);
            System.out.println("*** Methods with annotation " + getAnnotationName(method) + " ended");
        }
    }

    private Method[] getMethodsWithAnnotation(Method[] methods, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .toArray(Method[]::new);
    }

    private String getAnnotationName(Method method) {
        if (method.isAnnotationPresent(Before.class)) {
            return "Before";
        } else {
            return "After";
        }
    }
}
