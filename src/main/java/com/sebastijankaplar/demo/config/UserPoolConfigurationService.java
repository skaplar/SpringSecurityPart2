package com.sebastijankaplar.demo.config;

import org.springframework.stereotype.Service;

@Service
public class UserPoolConfigurationService {

    private final CustomAuthManagerResolver customAuthManagerResolver;
    private final UserPoolConfigurationRepository userPoolConfigurationRepository;
    private final ExternalWebSecurityConfig externalWebSecurityConfig;

    public UserPoolConfigurationService(CustomAuthManagerResolver customAuthManagerResolver,
                                        UserPoolConfigurationRepository userPoolConfigurationRepository,
                                        ExternalWebSecurityConfig externalWebSecurityConfig) {
        this.customAuthManagerResolver = customAuthManagerResolver;
        this.userPoolConfigurationRepository = userPoolConfigurationRepository;
        this.externalWebSecurityConfig = externalWebSecurityConfig;
    }

    public void save(UserPoolConfiguration userPoolConfiguration) {
        userPoolConfigurationRepository.save(userPoolConfiguration);
        customAuthManagerResolver.setIssuer(externalWebSecurityConfig.getAuthenticationManagerResolver());
    }
}
