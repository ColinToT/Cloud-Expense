package com.cloudexpense.security.provider;

import com.cloudexpense.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ClassName: DaoAuthenticationProviderConfig
 * Package: com.cloudexpense.security.provider
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/23 21:24
 * @Version: v1.0
 */
@Configuration
@RequiredArgsConstructor
public class DaoAuthenticationProviderConfig {

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }
}
