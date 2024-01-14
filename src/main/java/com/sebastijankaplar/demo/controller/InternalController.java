package com.sebastijankaplar.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sebastijankaplar.demo.config.UserPoolConfiguration;
import com.sebastijankaplar.demo.config.UserPoolConfigurationService;

@RestController
@RequestMapping("/api/internal/hello")
public class InternalController {

    private final UserPoolConfigurationService userPoolConfigurationService;

    public InternalController(UserPoolConfigurationService userPoolConfigurationService) {
        this.userPoolConfigurationService = userPoolConfigurationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String hello() {
        return "HELLO Internally!";
    }

    @PostMapping
    public String addManager() {
        userPoolConfigurationService.save(new UserPoolConfiguration("eu-west-1", "eu-west-1_identifier_no_2"));
        return "OK!";
    }

}
