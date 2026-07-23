package com.cloudexpense.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

/**
 * ClassName: AuthenticationConfig
 * Package: com.cloudexpense.security.config
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/23 21:35
 * @Version: v1.0
 */
@Configuration
public class AuthenticationConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

}
