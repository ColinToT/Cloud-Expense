package com.cloudexpense.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName: LoginRequest
 * Package: com.cloudexpense.user.dto
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/22 21:43
 * @Version: v1.0
 */
@Getter
@Setter
public class LoginRequest {

    private String email;

    private String password;
}
