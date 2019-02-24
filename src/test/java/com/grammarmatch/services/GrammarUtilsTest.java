package com.grammarmatch.services;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GrammarUtilsTest {

    @Test
    public void should_return_list_containing_5_groups() {
        String utterance = "a b c d";
        List<String> wordGroups = GrammarUtils.buildSlidingGroups( utterance, 3, 2);

        assertThat( wordGroups, hasSize(5));
    }


    @Test
    public void should_return_list_containing_all_subgroups() {
        String utterance = "a b c d";
        List<String> wordGroups = GrammarUtils.buildSlidingGroups( utterance, 3, 2);

        assertThat( wordGroups, hasItem("a b c"));
        assertThat( wordGroups, hasItem("b c d"));
        assertThat( wordGroups, hasItem("a b"));
        assertThat( wordGroups, hasItem("b c"));
        assertThat( wordGroups, hasItem("c d"));
    }

    @Test
    public void should_return_empty_list_when_empty_utterance() {
        String utterance = "";
        List<String> wordGroups = GrammarUtils.buildSlidingGroups( utterance, 3, 2);
        assertThat( wordGroups, empty() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_min_gt_max() {
        GrammarUtils.buildSlidingGroups("",5,6);
    }
}