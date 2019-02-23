package com.grammarmatch.services;


import com.grammarmatch.domain.MatchResult;

/**
 * A MatcherService maps an input string into a different string by "matching" it against one or more
 * transformation functions (such as a GrammarMatcher)
 */
public interface MatcherService {

    /**
     * matches an utterance (which is a space separated String of words) against a {@link GrammarRecognizer}
     *
     * @param utterance - a space separated String of words
     * @return MatchResult object that contains the overall result of all matches, plus details of each match. If
     * the utterance did not match anything, then the MatchResult.result will equal the original utterance
     */
    MatchResult match(String utterance);
}
