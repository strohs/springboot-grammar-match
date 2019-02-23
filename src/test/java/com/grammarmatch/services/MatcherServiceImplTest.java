package com.grammarmatch.services;

import com.grammarmatch.domain.MatchDetail;
import com.grammarmatch.domain.MatchResult;
import com.grammarmatch.services.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class MatcherServiceImplTest {

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
        service = new MatcherServiceImpl( pool, formatter );
        Locale.setDefault( Locale.US );
    }



    private MatchDetail buildMatchDetail(GrammarType gt, String in, String out) {
        return MatchDetail.builder().grammarName(gt.grammarName()).input(in).output(out).build();
    }

    private MatchResult buildMatchResult( GrammarType gt, String original, String result, MatchDetail ... details) {
        List<MatchDetail> mds = Arrays.asList( details );
        return MatchResult.builder().original(original).result(result).matchDetails(mds).build();
    }
}