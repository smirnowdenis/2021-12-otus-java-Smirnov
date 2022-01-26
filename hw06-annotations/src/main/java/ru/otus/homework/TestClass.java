package ru.otus.homework;

import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;

public class TestClass {

    private Integer a;

    private Integer b;

    @Before
    public void setUp() {
        System.out.println("* set up");
    }

    @ru.otus.homework.annotations.Test
    public void anyTest1() {
        System.out.println("   any test 1");
    }

    @ru.otus.homework.annotations.Test
    public void anyTest2() throws Exception {
        System.out.println("   any test 2");
        throw new Exception();
    }

    @ru.otus.homework.annotations.Test
    public void anyTest3() {
        a = 10;
        b = 5;
        System.out.println("   any test 3");
        if (a + b == 15) {
            System.out.println("   Sum equals 15");
        } else {
            throw new IllegalArgumentException("Bad result");
        }
    }

    @ru.otus.homework.annotations.Test
    public void anyTest4() {
        a = null;
        b = 8;
        System.out.println("   any test 4");
        if (a + b == 200) {
            System.out.println("Sum equals 200");
        } else {
            throw new IllegalArgumentException("Bad result");
        }
    }

    @After
    public void tearDown() {
        System.out.println("* tear down");
    }
}
