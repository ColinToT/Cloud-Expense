package com.cloudexpense.user.service.Impl;

import com.cloudexpense.common.exception.UserNotFoundException;
import com.cloudexpense.user.entity.User;
import com.cloudexpense.user.repository.UserRepository;
import com.cloudexpense.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ClassName: UserServiceImpl
 * Package: com.cloudexpense.user.service.Impl
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/22 21:46
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

}
