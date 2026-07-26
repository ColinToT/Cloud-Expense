package com.cloudexpense.user.service;

import com.cloudexpense.common.exception.UserNotFoundException;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * ClassName: CurrentUserService
 * Package: com.cloudexpense.user.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/26 18:18
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(
                UserNotFoundException::new
        );
    }

}
