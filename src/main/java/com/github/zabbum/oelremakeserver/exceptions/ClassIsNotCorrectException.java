package com.github.zabbum.oelremakeserver.exceptions;

public class ClassIsNotCorrectException extends RuntimeException {
    public ClassIsNotCorrectException(Class<?> clazz) {
        super("Class " + clazz.getName() + " is not correct class in this context.");
    }
}
