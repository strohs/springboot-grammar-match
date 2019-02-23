package com.grammarmatch.services;

import com.grammarmatch.domain.MatchDetail;
import com.grammarmatch.domain.MatchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GrammarMatcherServiceImpl implements MatcherService {


    private GrammarRecognizerPool pool;
    private GrammarFormatter formatter;

    public GrammarMatcherServiceImpl(GrammarRecognizerPool pool, GrammarFormatter formatter) {
        this.pool = pool;
        this.formatter = formatter;
    }


    @Override
    public MatchResult match(String inStr) {
        List<MatchDetail> details = new ArrayList<>();
        String tempStr = inStr.toLowerCase();
        GrammarRecognizer recognizer = null;
        try {
            recognizer = (GrammarRecognizer) pool.getTarget();
            // match the inputStr against all grammars, keeping track of the results of each match
            for (GrammarType gt : GrammarType.defaultOrder) {
                tempStr = getAllMatches(gt, details, tempStr, recognizer);
            }
        } catch (Exception e) {
            log.error("could not obtain GrammarRecognizer from pool: {}", e.getMessage() );
        } finally {
            try {
                pool.releaseTarget(recognizer);
            } catch (Exception e) {
                log.error("could not release recognizer back to pool: {}", e.getMessage() );
            }
        }
        return MatchResult.builder()
                .original(inStr)
                .result(tempStr)
                .matchDetails(details)
                .build();
    }

//    /**
//     * match the utterance against the a specific grammar
//     * @param gt- the specific GrammarType to match against
//     * @param utterance - a string of space separated words to match against the grammar
//     * @return - MatchResult object containing the details of the match, if any, otherwise if nothing
//     * matched, then the MatchResult.result will equal the input utterance
//     */
//    public MatchResult match( GrammarType gt, String utterance ) {
//        List<MatchDetail> details = new ArrayList<>();
//        String tempStr = utterance.toLowerCase();
//        GrammarRecognizer recognizer = null;
//        try {
//            recognizer = (GrammarRecognizer) pool.getTarget();
//            tempStr = getAllMatches(gt, details, tempStr, recognizer);
//        } catch (Exception e) {
//            log.error("could not obtain GrammarRecognizer from pool: {}", e.getMessage() );
//        } finally {
//            try {
//                pool.releaseTarget(recognizer);
//            } catch (Exception e) {
//                log.error("could not release recognizer back to pool: {}", e.getMessage() );
//            }
//        }
//        return MatchResult.builder()
//                .original(utterance)
//                .result(tempStr)
//                .matchDetails(details)
//                .build();
//    }

    private String getAllMatches(GrammarType gt, List<MatchDetail> details, String tempStr, GrammarRecognizer recognizer) {
        Optional<MatchDetail> md = findFirstMatch( recognizer, gt, tempStr);
        // while more matches are found
        while (md.isPresent()) {
            // add details of the match, reset tempStr = output of the match, and then check for more matches
            details.add( md.get() );
            tempStr = tempStr.replace( md.get().getInput(), md.get().getOutput() );
            md = findFirstMatch(recognizer, gt, tempStr);
        }
        return tempStr;
    }

    /**
     * attempts to find if any of the words in the utterance match the specified grammar. It does this by breaking
     * the utterance into smaller subsets of words using a "sliding window". The window slides over the utterance,
     * beginning with all the words in the utterance, and then taking smaller groups of words with each iteration.
     * The algorithm stops when the first match is found, or when all subset of words have been tried.
     *
     * @param recognizer - the {@link GrammarRecognizer} to user for matching
     * @param gt - the {@link GrammarType} to match against
     * @param utterance - a space separated String of words
     * @return an Optional<MatchDetail> of the first utterance that matches the grammar, if nothing matched,
     * Optional<Empty> is returned
     */
    private Optional<MatchDetail> findFirstMatch(GrammarRecognizer recognizer, GrammarType gt, String utterance ) {
        List<String> wordsGroups =  GrammarUtils.buildSlidingGroups(utterance, gt.getMaxWords(), gt.getMinWords());
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
