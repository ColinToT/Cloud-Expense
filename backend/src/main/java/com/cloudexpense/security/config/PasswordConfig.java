package com.cloudexpense.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ClassName: PasswordEncoder
 * Package: com.cloudexpense.security.config
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/23 20:26
 * @Version: v1.0
 */
@Configuration
public class PasswordConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
