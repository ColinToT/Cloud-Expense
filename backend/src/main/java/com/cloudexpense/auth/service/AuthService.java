package com.cloudexpense.auth.service;

import com.cloudexpense.auth.dto.LoginRequest;
import com.cloudexpense.auth.dto.LoginResponse;
import com.cloudexpense.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * ClassName: AuthService
 * Package: com.cloudexpense.auth.service
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/23 21:46
 * @Version: v1.0
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        authenticationManager.authenticate(authenticationToken);

        String token = jwtService.generateToken(request.email());

        return new LoginResponse(token);
    }

}
