package com.cliff.grammarmatch.services;

import com.sun.speech.engine.recognition.BaseRecognizer;
import edu.cmu.sphinx.tools.tags.ActionTagsParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.speech.EngineException;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component("grammarRecognizer")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GrammarRecognizerImpl implements GrammarRecognizer {

    private Recognizer recognizer;
    private Resource grammarFileDir;
    private List<String> grammarNames = new ArrayList<>();

    public GrammarRecognizerImpl(@Value("classpath:${grammar.files.dir}") Resource grammarFileDir) {
        this.grammarFileDir = grammarFileDir;
    }

    @PostConstruct
    @Override
    public void initialize() {
        //create recognizer
        BaseRecognizer recognizer = new BaseRecognizer();
        try {
            List<File> grammarFiles = loadGrammarFiles(grammarFileDir);
            recognizer.allocate();
            //load JSGF grammars
            for (File filePath : grammarFiles) {
                final RuleGrammar ruleGrammar = recognizer.loadJSGF(new FileReader(filePath));
                grammarNames.add(ruleGrammar.getName());
            }
            recognizer.commitChanges();
        } catch (IOException | EngineException | GrammarException e) {
            log.error("exception during GrammarRecognizer initialize: {}", e.getMessage() );
            throw new IllegalStateException(e);
        }

        this.recognizer = recognizer;
    }

    public String match(String grammarName, String utterance) {
        String out = utterance;
        RuleGrammar grammar = recognizer.getRuleGrammar(grammarName);

        try {
            grammar.setEnabled(true);
            recognizer.commitChanges();

            //replace special chars
            utterance = replaceChars(utterance);

            RuleParse p = grammar.parse(utterance, null);
            if (p != null) {
                //a match was found
                ActionTagsParser parser = new ActionTagsParser();
                parser.parseTags(p);
                out = (String) parser.get("$value");
            } else {
                out = this.NO_MATCH;
            }
            grammar.setEnabled(false);
            recognizer.commitChanges();
        } catch (GrammarException e) {
            log.error("could not match against grammar file: {}", e.getMessage() );
        }
        return out;
    }

    @Override
    public void shutdown() {
        try {
            for (String name : grammarNames) {
                recognizer.deleteRuleGrammar(recognizer.getRuleGrammar(name));
            }
            recognizer.commitChanges();
            recognizer.deallocate();
            log.debug("shutdown of grammar recognizer successful");
        } catch (Exception e) {
            log.error("exception occured deallocating a recognizer: {}", e.getMessage());
        }
    }

    private List<File> loadGrammarFiles(Resource grammarFileDir) throws IOException {
        log.debug("loading grammar files from: {}", grammarFileDir);
        final File[] files = grammarFileDir.getFile().listFiles((dir, name) -> name.endsWith(".gram"));
        assert files != null && files.length > 0;
        log.debug("found {} grammar files", files.length);
        return Arrays.asList(files);
    }

    //replace special characters that will throw off the grammar matcher and/or ecmascript parser
    private static String replaceChars(String in) {
        return in.replaceAll("['\"]", "");
    }
}
