package com.cliff.grammarmatch.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrammarRecognizerPool extends CommonsPool2TargetSource {


    public GrammarRecognizerPool(@Value("${recognizer.pool.max.size}") int maxSize,
                                 @Value("${recognizer.pool.min.idle}") int minIdle) {
        setMaxSize(maxSize);
        setMinIdle(minIdle);
        setTargetBeanName("grammarRecognizer");
        setTargetClass(GrammarRecognizer.class);
    }

    @Override
    public void destroyObject(PooledObject<Object> p) throws Exception {
        GrammarRecognizer gr = (GrammarRecognizer) p.getObject();
        gr.shutdown();
        super.destroyObject(p);
    }
}
