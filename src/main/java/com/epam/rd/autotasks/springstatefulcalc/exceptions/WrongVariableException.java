package com.epam.rd.autotasks.springstatefulcalc.exceptions;

public class WrongVariableException extends RuntimeException {

    public WrongVariableException(String message) {
        super(message);
    }
}