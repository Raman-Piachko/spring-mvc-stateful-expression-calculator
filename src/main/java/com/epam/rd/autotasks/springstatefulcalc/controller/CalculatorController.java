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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.EXPRESSION;

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
    public ResponseEntity<HttpStatus> getResult(ServletResponse resp, HttpSession session)
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
    public ResponseEntity<HttpStatus> putVariable(@PathVariable String variable, ServletRequest request, HttpSession session)
            throws IOException {
        String value = request.getReader().readLine();

        return sessionService.getResponseEntityByPutVariable(variable, session, value);
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


}