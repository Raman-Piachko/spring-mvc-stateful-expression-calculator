package com.epam.rd.autotasks.springstatefulcalc.service;

import java.util.Map;

public interface Calculator {
    int calculate(String expression, Map<String, String> attributeValueMap);
}