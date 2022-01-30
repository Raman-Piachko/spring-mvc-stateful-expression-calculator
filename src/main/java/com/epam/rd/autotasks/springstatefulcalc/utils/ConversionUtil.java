package com.epam.rd.autotasks.springstatefulcalc.utils;

import java.util.List;

import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.EMPTY_SYMBOL;
import static com.epam.rd.autotasks.springstatefulcalc.constants.ControllerConstants.SPACES;

public class ConversionUtil {

    private ConversionUtil() {
    }

    public static String deleteSpacesAndConvertListToString(List<String> expression) {
        return String.join(EMPTY_SYMBOL, expression)
                .replaceAll(SPACES, EMPTY_SYMBOL);
    }
}
