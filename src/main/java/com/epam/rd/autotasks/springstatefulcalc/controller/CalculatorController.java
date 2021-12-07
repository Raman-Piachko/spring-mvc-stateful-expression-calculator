package com.epam.rd.autotasks.springstatefulcalc.controller;

import com.epam.rd.autotasks.springstatefulcalc.service.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.ABS_RANGE;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.DIVIDE;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.EXPRESSION;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.MINUS;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.MULTIPLY;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.PLUS;

@Controller
@RequestMapping("/calc")
public class CalculatorController {

    private final Calculator calculator;

    @Autowired
    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping("/result")
    public ResponseEntity getResult(ServletResponse resp, HttpSession session)
            throws IOException {
        PrintWriter writer = resp.getWriter();
        String expression = (String) session.getAttribute(EXPRESSION);
        Map<String, String> attributeValueMap = getAttributeValueMap(session);
        try {
            writer.print(calculator.calculate(expression, attributeValueMap));
            writer.close();
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{variable}")
    public ResponseEntity putVariable(@PathVariable String variable, ServletRequest request, HttpSession session)
            throws IOException {
        String value = request.getReader().readLine();

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

    @DeleteMapping("/{variable}")
    public ResponseEntity deleteValue(@PathVariable("variable") String variable, HttpSession session) {
        session.setAttribute(variable, null);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private Map<String, String> getAttributeValueMap(HttpSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, String> attributeValueMap = new ConcurrentHashMap<>();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributeValueMap.put(attributeName, (String) session.getAttribute(attributeName));
        }

        return attributeValueMap;
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