package com.cloudexpense.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * ClassName: JwtService
 * Package: com.cloudexpense.security.jwt
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/25 9:00
 * @Version: v1.0
 */
@Service
public class JwtService {

    private final SecretKey secretKey;

    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}")String secret,
            @Value("${jwt.expiration}")long expiration
    ) {

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token){

        return extractAllClaims(token)
                .getSubject();
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, String email) {

        String username = extractUsername(token);
        return username.equals(email);
    }
}
