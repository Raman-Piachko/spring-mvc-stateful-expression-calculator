package com.epam.rd.autotasks.springstatefulcalc.advices;

import com.epam.rd.autotasks.springstatefulcalc.exceptions.WrongExpressionException;
import com.epam.rd.autotasks.springstatefulcalc.exceptions.WrongVariableException;
import com.google.code.mathparser.factories.exception.impl.InvalidCharacterException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CalcControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidCharacterException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String message = ex.getMessage();
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = WrongExpressionException.class)
    public ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        String message = ex.getMessage();
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = WrongVariableException.class)
    public ResponseEntity<Object> handleForbidden(RuntimeException ex, WebRequest request) {
        String message = ex.getMessage();
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}