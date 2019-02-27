package com.grammarmatch.api.v1.controllers;

import com.grammarmatch.domain.MatchResult;
import com.grammarmatch.services.MatcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( MatchController.BASE_URL )
@Slf4j
public class MatchController {

    public static final String BASE_URL = "/api/v1";
    private MatcherService matcher;

    public MatchController(MatcherService matcher) {
        this.matcher = matcher;
    }

    @GetMapping("/match/{inStr}")
    public MatchResult matchGet(@PathVariable String inStr ) {
        log.debug("GET match:{}", inStr);
        return matcher.match( inStr );
//        try {
//
//        } catch (GrammarException ge) {
//            throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, ge.getMessage(), ge );
//        }

    }

    @PostMapping("/match")
    public MatchResult matchPost(@RequestBody String inStr ) {
        log.debug("POST match:{}", inStr);
        return matcher.match( inStr );
    }


}
