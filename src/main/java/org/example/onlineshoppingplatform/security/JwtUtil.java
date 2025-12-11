package org.example.onlineshoppingplatform.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expMinutes:10080}")
    private long expMinutes;

    public String createToken(Long userId, String username, String role) {
        Algorithm alg = Algorithm.HMAC512(secret);
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer("onlineshoppingplatform")
                .withIssuedAt(java.util.Date.from(now))
                .withExpiresAt(java.util.Date.from(now.plusSeconds(expMinutes * 60)))
                .withClaim("uid", userId)
                .withClaim("username", username)
                .withClaim("role", role)
                .sign(alg);
    }

    public DecodedJWT verify(String token) {
        Algorithm alg = Algorithm.HMAC512(secret);
        return JWT.require(alg).withIssuer("onlineshoppingplatform").build().verify(token);
    }
}
