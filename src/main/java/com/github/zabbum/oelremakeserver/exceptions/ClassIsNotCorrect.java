package com.github.zabbum.oelremakeserver.exceptions;

public class ClassIsNotCorrect extends RuntimeException {
    public ClassIsNotCorrect(Class<?> clazz) {
        super("Class " + clazz.getName() + " is not correct class in this context.");
    }
}
