package com.sebastijankaplar.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public record UserPoolConfigurationRepository() {

    static List<UserPoolConfiguration> configurations = new ArrayList<>(
            // TODO: Replace eu-west-1_identifier with an actual id
//            List.of(new UserPoolConfiguration("eu-west-1", "eu-west-1_identifier"))
    );

    List<UserPoolConfiguration> findAll() {
        return configurations;
    }

    public void save(final UserPoolConfiguration configuration) {
        configurations.add(configuration);
    }
}
