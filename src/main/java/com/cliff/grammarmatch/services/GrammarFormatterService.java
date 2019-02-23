package com.cliff.grammarmatch.services;

import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;

/**
 * Utilities for formatting the Strings returned from grammars
 *
 * User: Cliff
 * Date: 4/6/2016
 * Time: 10:29 AM
 */
@Service
public class GrammarFormatterService implements GrammarFormatter {

    // the input date format returned by the DATE grammar
    private DateTimeFormatter inDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private DateTimeFormatter outDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    // the input time format returned by the TIME grammar
    private DateTimeFormatter inTimeFormatter = DateTimeFormatter.ofPattern("hhmma");
    private DateTimeFormatter outTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    public String format(GrammarType gt, String inStr) {
        switch (gt) {
            case DATE:
                return formatDate(inStr);
            case TIME:
                return formatTime(inStr);
            case CURRENCY:
                return formatCurrency(inStr);
            default:
                return inStr;
        }
    }

    private String formatTime(String inStr ) {
        String inTime = inStr.toUpperCase();
        final TemporalAccessor parsed = inTimeFormatter.parse(inTime);
        return outTimeFormatter.format(parsed);
    }


    private String formatDate( String inStr ) {
        final TemporalAccessor parsed = inDateFormatter.parse(inStr);
        return outDateFormatter.format(parsed);
    }

    private String formatCurrency( String inStr ) {
        return currencyFormat.format(Double.parseDouble(inStr));
    }
}
