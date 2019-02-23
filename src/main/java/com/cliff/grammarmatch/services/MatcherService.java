package com.cliff.grammarmatch.services;


import com.cliff.grammarmatch.domain.MatchResult;

import javax.speech.recognition.GrammarException;

/**
 * A MatcherService maps an input string into a different string by "matching" it against one or more
 * transformation functions (such as a GrammarMatcher)
 */
public interface MatcherService {

    /**
     * matches an utterance (which is a space separated String of words) against a {@link GrammarRecognizer}
     *
     * @param utterance - a space separated String of words
     * @return MatchResult object that contains the overall result of all matches, plus details of each match
     */
    MatchResult match(String utterance) throws GrammarException;
}
