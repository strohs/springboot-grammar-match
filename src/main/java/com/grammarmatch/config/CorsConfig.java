package com.grammarmatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This configuration will enable cross origin requests Globally. This is typically needed by
 * javascript single page applications that run on their own (separate) development servers
 *
 * NOTE: this is typically only needed for development and should be disabled in production
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    // Note that CORS has also been enabled in the SpringSecConfig
    @Override
    public void addCorsMappings( CorsRegistry registry ) {
        registry.addMapping( "/**" )
                .allowedOrigins( "*" )
                .allowedMethods( "HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE" )
                .maxAge( MAX_AGE_SECS );
    }
}