package com.epam.rd.autotasks.springstatefulcalc.service;

import com.google.code.mathparser.MathParser;
import com.google.code.mathparser.MathParserFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.EMPTY_SYMBOL;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ServiceConstants.ALPHABETICAL_REGEX;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ServiceConstants.CONTAINS_LETTER;
import static com.epam.rd.autotasks.springstatefulcalc.utils.ConversionUtil.deleteSpacesAndConvertListToString;

@Service
public class CalculatorServiceImpl implements Calculator {
    @Override
    public int calculate(String expression, Map<String, String> attributeValueMap) {
        MathParser mathParser = MathParserFactory.create();
        String finalExpression = getFinalExpression(expression, attributeValueMap);
        validateFinalExpression(finalExpression);

        return mathParser.calculate(finalExpression)
                .doubleValue()
                .intValue();
    }

    private String getFinalExpression(String expression, Map<String, String> attributeValueMap) {
        List<String> expressionList = Arrays.asList(expression.split(EMPTY_SYMBOL));

        while (isExpressionWithVariables(attributeValueMap, expressionList)) {
            convertExpressionWithValue(attributeValueMap, expressionList);
        }

        return deleteSpacesAndConvertListToString(expressionList);
    }


    private void convertExpressionWithValue(Map<String, String> parameters, List<String> expression) {
        for (String item : expression) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase(item)) {
                    int replaceableIndex = expression.indexOf(item);
                    expression.set(replaceableIndex, value);
                }
            }
        }
    }

    private void validateFinalExpression(String finalExpression) {
        boolean atLeastOneAlpha = finalExpression.matches(ALPHABETICAL_REGEX);
        if (atLeastOneAlpha) {
            throw new IllegalArgumentException(CONTAINS_LETTER);
        }
    }

    private boolean isExpressionWithVariables(Map<String, String> parameters, List<String> expression) {
        return expression.stream().anyMatch(parameters::containsKey);
    }
}