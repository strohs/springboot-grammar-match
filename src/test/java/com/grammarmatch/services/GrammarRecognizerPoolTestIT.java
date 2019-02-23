package com.grammarmatch.services;

import com.grammarmatch.services.GrammarRecognizer;
import com.grammarmatch.services.GrammarRecognizerPool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrammarRecognizerPoolTestIT {

    @Autowired
    private GrammarRecognizerPool pool;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void pool_is_created() throws Exception {
        assertThat( pool, is( notNullValue() ));
        final GrammarRecognizer target = (GrammarRecognizer) pool.getTarget();
        assertThat( target, is( notNullValue()) );
        pool.releaseTarget( target );
    }
}