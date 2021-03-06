package com.grammarmatch.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrammarRecognizerTestIT {

    @Autowired
    private GrammarRecognizer recognizer;

    @Test
    public void grammarRecognizer_initializes() {
        assertThat(recognizer, notNullValue());
    }

    @Test
    public void number_grammar_should_match_199000() {
        String result = recognizer.match( GrammarType.NUMBER.grammarName(), "one hundred ninety nine thousand");
        assertThat( result, is("199000"));
    }

    @Test
    public void number_grammar_should_return_NOMATCH() {
        String result = recognizer.match( GrammarType.NUMBER.grammarName(), "twelve oclock");
        assertThat( result, is("NOMATCH"));
    }

    @Test
    public void ordinal_grammar_should_match_199th() {
        String result = recognizer.match( GrammarType.ORDINAL.grammarName(), "one hundred ninety ninth");
        assertThat( result, is("199th"));
    }

    @Test
    public void date_grammar_should_match_19991105() {
        String result = recognizer.match( GrammarType.DATE.grammarName(), "november fifth nineteen ninety nine");
        assertThat( result, is("19991105"));
    }

    @Test
    public void time_grammar_should_match_12_47_pm() {
        String result = recognizer.match( GrammarType.TIME.grammarName(), "twelve forty seven pm");
        assertThat( result, is("1247pm"));
    }

    @Test
    public void time_grammar_should_match_12_47_am() {
        String result = recognizer.match( GrammarType.TIME.grammarName(), "twelve forty seven a.m.");
        assertThat( result, is("1247am"));
    }

    @Test
    public void currency_grammar_should_match_2_59() {
        String result = recognizer.match( GrammarType.CURRENCY.grammarName(), "two dollars and fifty nine cents");
        assertThat( result, is("2.59"));
    }
}
