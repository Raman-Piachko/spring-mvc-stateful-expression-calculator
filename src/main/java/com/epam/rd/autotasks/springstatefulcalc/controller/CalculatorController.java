package com.epam.rd.autotasks.springstatefulcalc.controller;

import com.epam.rd.autotasks.springstatefulcalc.service.Calculator;
import com.epam.rd.autotasks.springstatefulcalc.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.SESSION_KEY_EXPRESSION;

@Controller
@RequestMapping("/calc")
public class CalculatorController {

    private final Calculator calculator;
    private final SessionService sessionService;

    @Autowired
    public CalculatorController(Calculator calculator, SessionService sessionService) {
        this.calculator = calculator;
        this.sessionService = sessionService;
    }

    @GetMapping("/result")
    public ResponseEntity<Integer> getResult(HttpSession session) {
        String expression = (String) session.getAttribute(SESSION_KEY_EXPRESSION);
        Map<String, String> attributeValueMap = sessionService.getAttributeValueMap(session);
        int calculate = calculator.calculate(expression, attributeValueMap);

        return new ResponseEntity<>(calculate, HttpStatus.OK);
    }

    @PutMapping("/expression")
    public ResponseEntity<HttpStatus> putExpression(@RequestBody String value, HttpSession session) {
        boolean isVariableExist = sessionService.isVariableExists(SESSION_KEY_EXPRESSION, session);
        sessionService.addExpression(session, value);
        return getResponseEntity(isVariableExist);
    }

    @PutMapping("/{variable}")
    public ResponseEntity<HttpStatus> putVariable(@RequestBody String value, @PathVariable String variable, HttpSession session) {
        boolean isVariableExist = sessionService.isVariableExists(variable, session);
        sessionService.addVariable(variable, session, value);

        return getResponseEntity(isVariableExist);
    }

    @DeleteMapping("/{variable}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteValue(@PathVariable("variable") String variable, HttpSession session) {
        sessionService.deleteAttribute(session, variable);
    }

    private ResponseEntity<HttpStatus> getResponseEntity(boolean isVariableExist) {
        if (isVariableExist) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}