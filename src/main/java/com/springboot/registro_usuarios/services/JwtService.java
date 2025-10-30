package com.springboot.registro_usuarios.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.springboot.registro_usuarios.models.entities.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generatedToken(User user){
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String extractUsername(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }


    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().toInstant().isBefore(Instant.now());
    }

}
