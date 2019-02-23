package com.cliff.grammarmatch.services;

import java.util.Arrays;
import java.util.List;

/**
 * Enum that lists all the JSGF grammars supported by this application
 *
 * maxWords and minWords are used to help in matching sentences against a grammar file. They represent an upper
 * and a lower bound that can be used to limit the number of words matched against a grammar file. For example, trying
 * to match more than 15 words against the CURRENCY grammar will always return NOMATCH, or trying to match less than
 * two.
 *
 * maxWords is a count of the maximum number of "words" that would successfully match against the specified grammar
 * minWords is a count of the minimum number of "words" that would successfully match against the specified grammar
 */
public enum GrammarType {
    CURRENCY(15,2),
    DATE(8,2),
    NUMBER(9,1),
    ORDINAL(9,1),
    TIME(4,2);

    private int maxWords;
    private int minWords;

    static final List<GrammarType> defaultOrder = Arrays.asList(
            GrammarType.CURRENCY, GrammarType.DATE,
            GrammarType.TIME, GrammarType.ORDINAL, GrammarType.NUMBER );

    GrammarType(int maxWords, int minWords) {
        this.maxWords = maxWords;
        this.minWords = minWords;
    }

    public String grammarName() { return this.name().toLowerCase(); }

    /**
     * returns a List indicating the order in which grammars should be run, in order to match an utterance
     * correctly
     */
    public List<GrammarType> getDefaultGrammarOrder() {
        return defaultOrder;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public int getMinWords() {
        return minWords;
    }}
