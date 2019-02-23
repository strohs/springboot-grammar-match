package com.grammarmatch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchDetail {

    private String grammarName;
    private String input;
    private String output;
}
