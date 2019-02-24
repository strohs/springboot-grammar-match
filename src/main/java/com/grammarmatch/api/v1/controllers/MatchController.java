package com.grammarmatch.api.v1.controllers;

import com.grammarmatch.domain.MatchResult;
import com.grammarmatch.services.MatcherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( MatchController.BASE_URL )
public class MatchController {

    public static final String BASE_URL = "/api/v1";
    private MatcherService matcher;

    public MatchController(MatcherService matcher) {
        this.matcher = matcher;
    }

    @GetMapping("/match/{inStr}")
    public MatchResult match( @PathVariable String inStr ) {
        return matcher.match( inStr );
//        try {
//
//        } catch (GrammarException ge) {
//            throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, ge.getMessage(), ge );
//        }

    }
}
