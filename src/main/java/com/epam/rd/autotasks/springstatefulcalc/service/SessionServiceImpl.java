package com.epam.rd.autotasks.springstatefulcalc.service;

import com.epam.rd.autotasks.springstatefulcalc.exceptions.WrongExpressionException;
import com.epam.rd.autotasks.springstatefulcalc.exceptions.WrongVariableException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.DIVIDE;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.MAX_ABS_VALUE_FOR_VARIABLE;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.MINUS;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.MULTIPLY;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.PLUS;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.SESSION_KEY_EXPRESSION;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ServiceConstants.BAD_EXPRESSION;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ServiceConstants.OVER_LIMIT;

@Service
public class SessionServiceImpl implements SessionService {
    public void addExpression(HttpSession session, String value) {
        if (!isGoodFormatExpression(value)) {
            throw new WrongExpressionException(BAD_EXPRESSION);
        }
        addValue(SESSION_KEY_EXPRESSION, session, value);
    }

    public void addVariable(String variable, HttpSession session, String value) {
        if (isParameterHasOverLimitValue(value)) {
            throw new WrongVariableException(String.format(OVER_LIMIT, variable, value));
        }
        addValue(variable, session, value);
    }

    public void deleteAttribute(HttpSession session, String variable) {
        session.removeAttribute(variable);
    }

    public Map<String, String> getAttributeValueMap(HttpSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, String> attributeValueMap = new HashMap<>();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributeValueMap.put(attributeName, (String) session.getAttribute(attributeName));
        }

        return attributeValueMap;
    }

    private void addValue(String variable, HttpSession session, String value) {
        session.setAttribute(variable, value);
    }

    private boolean isGoodFormatExpression(String expression) {
        return expression.contains(PLUS) || expression.contains(MINUS) || expression.contains(DIVIDE) || expression.contains(MULTIPLY);
    }

    public boolean isVariableExists(String variable, HttpSession session) {
        return session.getAttribute(variable) != null;
    }

    private boolean isParameterHasOverLimitValue(String paramValue) {
        try {
            int i = Integer.parseInt(paramValue);
            return Math.abs(i) > MAX_ABS_VALUE_FOR_VARIABLE;
        } catch (Exception e) {
            return false;
        }
    }
}