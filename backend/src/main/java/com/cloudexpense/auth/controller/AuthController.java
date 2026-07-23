package com.cloudexpense.auth.controller;

import com.cloudexpense.auth.dto.LoginRequest;
import com.cloudexpense.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: AuthController
 * Package: com.cloudexpense.auth.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/23 21:50
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        authService.login(request);
        return ResponseEntity.ok().build();
    }
}
