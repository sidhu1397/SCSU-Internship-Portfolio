package com.finalProject.GraphQL.StudentBFF.Config;


import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class TestConfig {

    @Autowired
    private Environment env;

    public Environment getEnv() {
        return env;
    }

}
