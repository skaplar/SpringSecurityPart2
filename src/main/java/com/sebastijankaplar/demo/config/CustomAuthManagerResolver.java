package com.sebastijankaplar.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class CustomAuthManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private JwtIssuerAuthenticationManagerResolver customIssuerAuthenticationManagerResolver;

    public CustomAuthManagerResolver() {
        Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();
        this.customIssuerAuthenticationManagerResolver = new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);
    }

    @Override
    public AuthenticationManager resolve(final HttpServletRequest request) {
        return customIssuerAuthenticationManagerResolver.resolve(request);
    }

    public void setIssuer(JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver) {
        this.customIssuerAuthenticationManagerResolver = jwtIssuerAuthenticationManagerResolver;
    }

}
