package com.cloudexpense.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * ClassName: LoginRequest
 * Package: com.cloudexpense.auth.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/23 21:43
 * @Version: v1.0
 */
public record LoginRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
