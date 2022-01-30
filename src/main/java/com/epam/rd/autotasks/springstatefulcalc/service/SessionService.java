package com.epam.rd.autotasks.springstatefulcalc.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface SessionService {
    void deleteAttribute(HttpSession session, String variable);

    Map<String, String> getAttributeValueMap(HttpSession session);

    boolean isVariableExists(String variable, HttpSession session);

    void addVariable(String variable, HttpSession session, String value);

    void addExpression(HttpSession session, String value);
}