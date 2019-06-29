package com.larvaesoft.opengst.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import timber.log.Timber;

public class Formatter {
    public static String doubleToString(double d) {
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        final DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(12);
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setGroupingUsed(true);
        return formatter.format(d);
    }

    public static Double stringToDouble(String str) {
        str = str.replaceAll(",", "");
        return Double.parseDouble(str);
    }

    public static String removeComma(String number) {
        return number.replaceAll(",", "");
    }

    public static String formatToTwo(double d) {
        Timber.d("formatting " + String.valueOf(d));
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        final DecimalFormat formatter = new DecimalFormat("######0.00");
        formatter.setMaximumFractionDigits(12);
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setGroupingUsed(true);
        return formatter.format(d);
    }
}
