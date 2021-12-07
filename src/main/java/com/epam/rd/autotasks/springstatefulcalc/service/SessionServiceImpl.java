package com.epam.rd.autotasks.springstatefulcalc.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.ABS_RANGE;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.DIVIDE;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.EXPRESSION;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.MINUS;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.MULTIPLY;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.PLUS;

public class SessionServiceImpl implements SessionService{
    public ResponseEntity getResponseEntityByPutVariable(String variable, HttpSession session, String value) {
        if (EXPRESSION.equalsIgnoreCase(variable)) {
            if (isGoodFormatExpression(value)) {
                return addData(variable, session, value);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            if (!isParameterHasOverLimitValue(value)) {
                return addData(variable, session, value);
            } else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        }
    }

    private ResponseEntity addData(String variable, HttpSession session, String value) {
        if (session.getAttribute(variable) == null) {
            session.setAttribute(variable, value);
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            session.setAttribute(variable, value);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    private boolean isGoodFormatExpression(String expression) {
        return (expression.contains(PLUS) || expression.contains(MINUS) ||
                expression.contains(DIVIDE) || expression.contains(MULTIPLY));
    }

    private boolean isParameterHasOverLimitValue(String paramValue) {
        try {
            int i = Integer.parseInt(paramValue);
            return Math.abs(i) > ABS_RANGE;
        } catch (Exception e) {
            return false;
        }
    }
}
