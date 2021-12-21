package com.epam.rd.autotasks.springstatefulcalc.exceptions;

public class WrongExpressionException extends RuntimeException {
    public WrongExpressionException(String message) {
        super(message);
    }
}