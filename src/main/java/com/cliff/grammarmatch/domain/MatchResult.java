package com.cliff.grammarmatch.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * holds overall details of all Matches, plus information on each individual match.
 */
@Data
@Builder
public class MatchResult {

    private String original;
    private String result;

    private List<MatchDetail> matchDetails;
}
