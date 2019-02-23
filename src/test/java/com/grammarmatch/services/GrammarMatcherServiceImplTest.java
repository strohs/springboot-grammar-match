package com.grammarmatch.services;

import com.grammarmatch.domain.MatchDetail;
import com.grammarmatch.domain.MatchResult;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

public class GrammarMatcherServiceImplTest {

    private MatcherService service;

    @Mock
    private GrammarFormatter formatter;

    @Mock
    private GrammarRecognizerPool pool;

    @Mock
    private GrammarRecognizer recognizer;

    @Mock
    private Logger logger;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        service = new GrammarMatcherServiceImpl( pool, formatter );

    }

    @Test
    public void should_return_matchResult_with_result_equal_original_when_utterance_doesnt_match_any_grammars() throws Exception {
        given( pool.getTarget()).willReturn( recognizer );
        given( recognizer.match( anyString(), anyString() )).willReturn("NOMATCH");

        final MatchResult mr = service.match("this utterance doesn't match any grammars");

        assertThat( mr.getResult(), is(mr.getOriginal()));
        assertThat( mr.getMatchDetails(), empty());
    }

    @Test
    public void should_always_return_a_matchResult_when_pool_throws_exception() throws Exception {
        given( pool.getTarget()).willThrow(new Exception());

        final MatchResult mr = service.match("some utterance");

        assertThat( mr.getResult(), is(mr.getOriginal()));
        assertThat( mr.getMatchDetails(), empty());
    }

    @Test
    public void should_obtain_recognizer_from_pool_one_time() throws Exception {
        given( pool.getTarget()).willReturn( recognizer );
        given( recognizer.match( anyString(), anyString() )).willReturn( GrammarRecognizer.NO_MATCH );

        final MatchResult mr = service.match("some utterance");

        then( pool ).should().getTarget();
    }

    @Test
    public void should_return_recognizer_to_pool_one_time() throws Exception {
        given( pool.getTarget()).willReturn( recognizer );
        given( recognizer.match( anyString(), anyString() )).willReturn( GrammarRecognizer.NO_MATCH );

        final MatchResult mr = service.match("some utterance");

        then( pool ).should().releaseTarget( recognizer );
    }

    @Test
    public void should_return_a_MatchResult_with_one_MatchDetail() throws Exception {
        String gramName = GrammarType.DATE.grammarName();
        String utt = "march first two thousand";
        String formatted = "Mar 1,2000";
        given( pool.getTarget()).willReturn( recognizer );
        given( formatter.format( GrammarType.DATE, "20000301") ).willReturn("Mar 1, 2000");
        given( recognizer.match( anyString(), anyString())).will(invocation ->
            (invocation.getArgument(0).equals( gramName ) && invocation.getArgument(1).equals(utt))
                    ? "20000301"
                    : GrammarRecognizer.NO_MATCH
        );

        final MatchResult mr = service.match( utt );

        assertThat( mr.getResult(), is("Mar 1, 2000"));
        assertThat( mr.getMatchDetails(), hasSize(1));
    }

    private MatchDetail buildMatchDetail(GrammarType gt, String in, String out) {
        return MatchDetail.builder().grammarName(gt.grammarName()).input(in).output(out).build();
    }

    private MatchResult buildMatchResult( GrammarType gt, String original, String result, MatchDetail ... details) {
        List<MatchDetail> mds = Arrays.asList( details );
        return MatchResult.builder().original(original).result(result).matchDetails(mds).build();
    }
}