package com.grammarmatch.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Aspect
@Slf4j
public class TimingAspect {

    @Pointcut("execution(com.grammarmatch.domain.MatchResult com.grammarmatch.services.MatcherService.match*(..))")
    public void matchMethods() {}

    // time the matcher service method
    @Around("matchMethods()")
    public Object matchTimer(ProceedingJoinPoint jp) {
        Object retVal = null;
        Instant start = Instant.now();
        try {
            retVal = jp.proceed();
        } catch (Throwable throwable) {
            log.error("MatcherService.match exception:{}",throwable.getMessage());
        }
        log.debug("MatchService match elapsed time: {}ms", Duration.between(start, Instant.now()).toMillis());
        return retVal;
    }
}
