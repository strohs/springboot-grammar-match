package com.grammarmatch.services;

import com.grammarmatch.domain.MatchDetail;
import com.grammarmatch.domain.MatchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.speech.recognition.GrammarException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MatcherServiceImpl implements MatcherService {


    private GrammarRecognizerPool pool;
    private GrammarFormatter formatter;

    public MatcherServiceImpl(GrammarRecognizerPool pool, GrammarFormatter formatter) {
        this.pool = pool;
        this.formatter = formatter;
    }


    @Override
    public MatchResult match(String inStr) throws GrammarException {
        List<MatchDetail> details = new ArrayList<>();
        String output = inStr.toLowerCase();
        // match the inputStr against all grammars, keeping track of the results of each match
        for (GrammarType gt : GrammarType.defaultOrder) {
            Optional<MatchDetail> md = findFirstMatch(gt, output);
            while (md.isPresent()) {
                details.add( md.get() );
                // replace the found match in the original input string
                output = output.replace( md.get().getInput(), md.get().getOutput() );
                md = findFirstMatch(gt, output);
            }
        }
        return MatchResult.builder()
                .original(inStr)
                .result(output)
                .matchDetails(details)
                .build();
    }

    /**
     * attempts to find if any of the words in the utterance match the specified grammar. It does this by breaking
     * the utterance into smaller subsets of words using a "sliding window". The window slides over the utterance,
     * beginning with all the words in the utterance, and then taking smaller groups of words with each iteration.
     * The algorithm stops when the first match is found, or when all subset of words have been tried.
     *
     * @param gt - the {@link GrammarType} to match against
     * @param utterance - a space separated String of words
     * @return an Optional<MatchDetail> of the first utterance that matches the grammar, if nothing matched,
     * Optional<Empty> is returned
     */
    private Optional<MatchDetail> findFirstMatch(GrammarType gt, String utterance ) {
        List<String> wordsGroups =  GrammarUtils.buildSlidingGroups(utterance, gt.getMaxWords(), gt.getMinWords());
        GrammarRecognizer recognizer = null;
        try {
            recognizer = (GrammarRecognizer) pool.getTarget();
            for (String wg : wordsGroups ) {
                String matchStr = recognizer.match(gt.grammarName(), wg);
                if ( !matchStr.equals(GrammarRecognizer.NO_MATCH) ) {
                    return Optional.of(MatchDetail.builder()
                            .grammarName(gt.grammarName())
                            .input(wg)
                            .output( formatter.format(gt, matchStr))
                            .build());
                }
            }
        } catch (Exception e) {
            log.error("exception occurred getting recognizer from the pool: {}", e.getMessage() );
        } finally {
            try {
                pool.releaseTarget(recognizer);
            } catch (Exception e) {
                log.error("could not release recognizer back to pool: {}", e.getMessage() );
            }
        }
        return Optional.empty();
    }

    private MatchDetail buildMatchDetail(GrammarType gramType, String orig, String res ) {
        return MatchDetail.builder()
                .input(orig)
                .output(res)
                .grammarName( gramType.grammarName() )
                .build();
    }
}
