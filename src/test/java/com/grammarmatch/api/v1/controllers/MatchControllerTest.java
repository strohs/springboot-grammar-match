package com.grammarmatch.api.v1.controllers;

import com.grammarmatch.domain.MatchDetail;
import com.grammarmatch.domain.MatchResult;
import com.grammarmatch.services.GrammarType;
import com.grammarmatch.services.MatcherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith( SpringRunner.class )
@WebMvcTest(controllers = {MatchController.class})
public class MatchControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MatcherService matcherService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void should_return_OK_with_MatchResult_for_GET_matching_any_utterance() throws Exception {
        String utt = "some random utterance";
        MatchResult mr = MatchResult.builder()
                .original(utt)
                .result(utt)
                .matchDetails( Collections.emptyList() )
                .build();

        given( matcherService.match( utt ) ).willReturn(mr);

        mockMvc.perform( get( MatchController.BASE_URL + "/match/" + utt))
                .andExpect( status().isOk() )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect( jsonPath("$.result").value(utt))
                .andExpect( jsonPath("$.original").value(utt));
    }

    @Test
    public void should_return_OK_with_empty_MatchDetail_when_GET_nothing_matches() throws Exception {
        String utt = "some random utterance";
        MatchResult mr = MatchResult.builder()
                .original(utt)
                .result(utt)
                .matchDetails( Collections.emptyList() )
                .build();

        given( matcherService.match( utt ) ).willReturn(mr);

        mockMvc.perform( get( MatchController.BASE_URL + "/match/" + utt))
                .andExpect( status().isOk() )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect( jsonPath("$.matchDetails").isEmpty());
    }


    @Test
    public void should_return_OK_with_MatchDetail_when_GET_an_utterance_matches() throws Exception {
        String utt = "my time is three fifty four p m";
        String match = "three fifty four p m";
        String formattedMatch = "3:54pm";
        MatchDetail md = MatchDetail.builder()
                .grammarName(GrammarType.TIME.grammarName())
                .input( match )
                .output( formattedMatch )
                .build();
        MatchResult mr = MatchResult.builder()
                .original(utt)
                .result(utt)
                .matchDetails(Arrays.asList(md))
                .build();

        given( matcherService.match( utt ) ).willReturn(mr);

        mockMvc.perform( get( MatchController.BASE_URL + "/match/" + utt))
                .andExpect( status().isOk() )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect( jsonPath("$.matchDetails[0].grammarName").value(GrammarType.TIME.grammarName()))
                .andExpect( jsonPath("$.matchDetails[0].input").value( match ))
                .andExpect( jsonPath("$.matchDetails[0].output").value( formattedMatch ));
    }

    @Test
    public void should_return_OK_with_MatchDetail_when_POSTing_utterance_that_matches_a_grammar() throws Exception {
        String utt = "my time is three fifty four p m";
        String match = "three fifty four p m";
        String formattedMatch = "3:54pm";
        MatchDetail md = MatchDetail.builder()
                .grammarName(GrammarType.TIME.grammarName())
                .input( match )
                .output( formattedMatch )
                .build();
        MatchResult mr = MatchResult.builder()
                .original(utt)
                .result(utt)
                .matchDetails(Arrays.asList(md))
                .build();

        given( matcherService.match( utt ) ).willReturn(mr);

        mockMvc.perform( post( MatchController.BASE_URL + "/match/").content(utt))
                .andExpect( status().isOk() )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect( jsonPath("$.matchDetails[0].grammarName").value(GrammarType.TIME.grammarName()))
                .andExpect( jsonPath("$.matchDetails[0].input").value( match ))
                .andExpect( jsonPath("$.matchDetails[0].output").value( formattedMatch ));
    }

    @Test
    public void should_return_OK_with_MatchResult_when_POST_utterance_that_doesnt_match_a_grammar() throws Exception {
        String utt = "utterance doesn't match";
        MatchResult mr = MatchResult.builder()
                .original(utt)
                .result(utt)
                .matchDetails(Collections.emptyList())
                .build();

        given( matcherService.match( utt ) ).willReturn(mr);

        mockMvc.perform( post( MatchController.BASE_URL + "/match/").content(utt))
                .andExpect( status().isOk() )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect( jsonPath("$.result").value(utt))
                .andExpect( jsonPath("$.original").value(utt));
    }
}