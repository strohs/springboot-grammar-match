package com.grammarmatch.controllers.v1;

import com.grammarmatch.domain.MatchResult;
import com.grammarmatch.services.MatcherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.speech.recognition.GrammarException;

@RestController
@RequestMapping( MatchController.BASE_URL )
public class MatchController {

    public static final String BASE_URL = "api/v1";
    private MatcherService matcher;

    public MatchController(MatcherService matcher) {
        this.matcher = matcher;
    }

    @GetMapping("/match/{inStr}")
    public MatchResult match( @PathVariable String inStr ) {
        try {
            return matcher.match( inStr );
        } catch (GrammarException ge) {
            throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, ge.getMessage(), ge );
        }

    }
}
