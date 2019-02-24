package com.grammarmatch.services;

/**
 * A JSAPI {@code javax.speech.recognition.Recognizer} that can be used to match text against a speech grammar file.
 */
public interface GrammarRecognizer {

    static String NO_MATCH = "NOMATCH";

    /**
     * creates a single {@code Recognizer} capable of matching Strings against one or more grammar files
     * @return a single {@code javax.speech.recognition.Recognizer} capable of matching Strings against it's grammars
     * @throws IllegalStateException wraps one of the three exceptions below
     *  EngineException - if the Recognizer could not be implemented
     *  IOException - if any of the grammar files could not be loaded
     *  GrammarException - if an error occured processing/compiling one of the grammar files
     */
    void initialize();

    /**
     * Attempts to match the utterance against the specified grammar.
     *
     * @param grammarName - the name of the grammar file to match against. This name must match a grammar name
     *                    that has already been loaded into this recognizer
     * @param utterance - a space separated String of words to match against
     * @return a string containing the result of the grammar if the utterence matched, else the "NOMATCH" string
     * will be returned, indicating the utterance did not match the specified grammar
     */
    String match(String grammarName, String utterance);

    /**
     * shutdown this recognizer and free any resources it has created
     */
    void shutdown();
}
