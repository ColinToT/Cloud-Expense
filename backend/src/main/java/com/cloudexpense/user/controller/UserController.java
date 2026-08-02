package com.cloudexpense.user.controller;

import com.cloudexpense.user.dto.UserResponse;
import com.cloudexpense.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: UserController
 * Package: com.cloudexpense.user.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/26 20:44
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(
                UserResponse.from(currentUserService.getCurrentUser())
        );
    }
}
