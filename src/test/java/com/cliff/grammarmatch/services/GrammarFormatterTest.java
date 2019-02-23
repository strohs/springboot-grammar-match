package com.cliff.grammarmatch.services;

import com.cliff.grammarmatch.services.GrammarFormatter;
import com.cliff.grammarmatch.services.GrammarFormatterService;
import com.cliff.grammarmatch.services.GrammarType;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GrammarFormatterTest {

    GrammarFormatter grammarFormatter = new GrammarFormatterService();

    @Before
    public void setUp() throws Exception {
        // force locale to US for these unit tests
        Locale.setDefault( Locale.US );
    }

    @Test
    public void test_time_formatter() {
        final String s = grammarFormatter.format(GrammarType.TIME, "1247pm");
        assertThat( s, is("12:47 PM"));
    }

    @Test
    public void test_date_formatter() {
        final String s = grammarFormatter.format(GrammarType.DATE, "20181114");
        assertThat( s, is("Nov 14, 2018"));
    }

    @Test
    public void test_currency_formatter() {
        final String s = grammarFormatter.format(GrammarType.CURRENCY, "24242456.11");
        assertThat( s, is("$24,242,456.11"));
    }

}
