package com.grammarmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class GrammarMatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrammarMatchApplication.class, args);
    }

}
