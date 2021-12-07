package com.epam.rd.autotasks.springstatefulcalc.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

public interface SessionService {
    ResponseEntity<HttpStatus> getResponseEntityByPutVariable(String variable, HttpSession session, String value);
}
