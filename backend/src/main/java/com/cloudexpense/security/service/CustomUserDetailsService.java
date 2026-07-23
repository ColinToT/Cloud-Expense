package com.cloudexpense.security.service;

import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ClassName: CustomUserDetailsService
 * Package: com.cloudexpense.security.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/23 20:30
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found")
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .build();
    }
}
