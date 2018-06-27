package com.example.demo;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api")
public class JerseyApplication extends ResourceConfig {
    public JerseyApplication() {
        packages("com.example.demo");
    }
}
