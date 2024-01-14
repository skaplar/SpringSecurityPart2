package com.sebastijankaplar.demo.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ExternalWebSecurityConfig {

    private final String COGNITO_BASE_URL = "https://cognito-idp.%s.amazonaws.com/%s";

    private final CustomAuthManagerResolver customAuthManagerResolver;
    private final UserPoolConfigurationRepository userPoolConfigurationRepository;

    public ExternalWebSecurityConfig(CustomAuthManagerResolver customAuthManagerResolver, UserPoolConfigurationRepository userPoolConfigurationRepository) {
        this.customAuthManagerResolver = customAuthManagerResolver;
        this.userPoolConfigurationRepository = userPoolConfigurationRepository;
    }

    @Bean
    public SecurityFilterChain externalFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/external/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/external/**")
                        .authenticated()
                        .anyRequest().permitAll()
                ).csrf(CsrfConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2ResourceServer -> {
                    customAuthManagerResolver.setIssuer(getAuthenticationManagerResolver());
                    oauth2ResourceServer.authenticationManagerResolver(customAuthManagerResolver);
                });
        return http.build();
    }

    public JwtIssuerAuthenticationManagerResolver getAuthenticationManagerResolver() {
        Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

        List<UserPoolConfiguration> userPoolConfigurationList = userPoolConfigurationRepository.findAll();

        userPoolConfigurationList.forEach(userPoolConfiguration -> {
            String poolConfig = String.format(COGNITO_BASE_URL, userPoolConfiguration.region(), userPoolConfiguration.userPoolId());
            addManager(authenticationManagers, poolConfig);
        });

        return new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);
    }

    public void addManager(Map<String, AuthenticationManager> authenticationManagers, String issuer) {
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(JwtDecoders.fromOidcIssuerLocation(issuer));
        authenticationManagers.put(issuer, authenticationProvider::authenticate);
    }

}
