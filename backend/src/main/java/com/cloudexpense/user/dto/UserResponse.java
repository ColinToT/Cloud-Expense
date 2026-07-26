package com.cloudexpense.user.dto;

import com.cloudexpense.user.entity.Role;
import com.cloudexpense.user.entity.User;

/**
 * ClassName: UserResponse
 * Package: com.cloudexpense.user.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/26 18:13
 * @Version: v1.0
 */
public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}
