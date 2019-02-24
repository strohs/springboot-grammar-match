package com.grammarmatch.services;

import com.grammarmatch.domain.MatchDetail;
import com.grammarmatch.domain.MatchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class GrammarMatcherServiceImplTest {

    private MatcherService service;

    @Mock
    private GrammarFormatter formatter;

    @Mock
    private GrammarRecognizerPool pool;

    @Mock
    private GrammarRecognizer recognizer;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        service = new GrammarMatcherServiceImpl( pool, formatter );

    }

    @Test
    public void should_return_matchResult_equalTo_original_utterance_when_nothing_matches() throws Exception {
        given( pool.getTarget()).willReturn( recognizer );
        given( recognizer.match( anyString(), anyString() )).willReturn("NOMATCH");

        final MatchResult mr = service.match("this utterance doesn't match any grammars");

        assertThat( mr.getResult(), is(mr.getOriginal()));
        assertThat( mr.getMatchDetails(), empty());
    }

    @Test
    public void should_return_a_matchResult_when_pool_throws_exception() throws Exception {
        given( pool.getTarget()).willThrow(new Exception());

        final MatchResult mr = service.match("some utterance");

        assertThat( mr.getResult(), is(mr.getOriginal()));
        assertThat( mr.getMatchDetails(), empty());
    }

    @Test
    public void should_obtain_one_recognizer_from_pool_per_match_request() throws Exception {
        given( pool.getTarget()).willReturn( recognizer );
        given( recognizer.match( anyString(), anyString() )).willReturn( GrammarRecognizer.NO_MATCH );

        final MatchResult mr = service.match("some utterance");

        then( pool ).should().getTarget();
    }

    @Test
    public void should_return_recognizer_to_pool_when_match_call_is_finished() throws Exception {
        given( pool.getTarget()).willReturn( recognizer );
        given( recognizer.match( anyString(), anyString() )).willReturn( GrammarRecognizer.NO_MATCH );

        final MatchResult mr = service.match("some utterance");

        then( pool ).should().releaseTarget( recognizer );
    }

    @Test
    public void should_call_GrammarFormatter_when_any_utterance_matches_a_grammar() throws Exception {
        String gramName = GrammarType.DATE.grammarName();
        String utt = "march first two thousand";
        String dateResult = "20000301";
        String formattedResult = "Mar 1, 2000";
        given( pool.getTarget()).willReturn( recognizer );
        given( formatter.format( any(), anyString() ) ).willReturn( formattedResult );
        given( recognizer.match( anyString(), anyString())).will(invocation ->
                (invocation.getArgument(0).equals( gramName ) && invocation.getArgument(1).equals(utt))
                        ? dateResult
                        : GrammarRecognizer.NO_MATCH
        );

        final MatchResult mr = service.match( utt );

        then(formatter).should().format( GrammarType.DATE, dateResult );
    }

    @Test
    public void should_return_a_MatchResult_with_formatted_output_when_utterance_matches() throws Exception {
        String gramName = GrammarType.DATE.grammarName();
        String utt = "march first two thousand";
        String gramResult = "20000301";
        String formattedResult = "Mar 1, 2000";
        given( pool.getTarget()).willReturn( recognizer );
        given( formatter.format( GrammarType.DATE, gramResult ) ).willReturn( formattedResult );
        given( recognizer.match( anyString(), anyString())).will(invocation ->
                (invocation.getArgument(0).equals( gramName ) && invocation.getArgument(1).equals(utt))
                        ? gramResult
                        : GrammarRecognizer.NO_MATCH
        );

        final MatchResult mr = service.match( utt );

        assertThat( mr.getResult(), is( formattedResult ));
        assertThat( mr.getOriginal(), is( utt ));
    }

    @Test
    public void should_return_MatchDetails_when_a_grammar_matches() throws Exception {
        String gramName = GrammarType.DATE.grammarName();
        String utt = "march first two thousand";
        String dateResult = "20000301";
        String formattedResult = "Mar 1, 2000";
        given( pool.getTarget()).willReturn( recognizer );
        given( recognizer.match( anyString(), anyString())).will(invocation ->
            (invocation.getArgument(0).equals( gramName ) && invocation.getArgument(1).equals(utt))
                    ? dateResult
                    : GrammarRecognizer.NO_MATCH
        );
        given( formatter.format( GrammarType.DATE, dateResult ) ).willReturn( formattedResult );

        final MatchResult mr = service.match( utt );

        assertThat( mr, is( notNullValue() ));
        assertThat( mr.getMatchDetails(), hasSize(1));
        assertThat( mr.getMatchDetails().get(0).getGrammarName(), is(GrammarType.DATE.grammarName()) );
        assertThat( mr.getMatchDetails().get(0).getInput(), is( utt ) );
        assertThat( mr.getMatchDetails().get(0).getOutput(), is(formattedResult) );
    }



    private MatchDetail buildMatchDetail(GrammarType gt, String in, String out) {
        return MatchDetail.builder().grammarName(gt.grammarName()).input(in).output(out).build();
    }

    private MatchResult buildMatchResult( GrammarType gt, String original, String result, MatchDetail ... details) {
        List<MatchDetail> mds = Arrays.asList( details );
        return MatchResult.builder().original(original).result(result).matchDetails(mds).build();
    }
}