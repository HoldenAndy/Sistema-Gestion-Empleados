package com.springboot.registro_usuarios.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.springboot.registro_usuarios.models.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generatedToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Instant issuedAtInstant = Instant.now();
        Date issuedAt = Date.from(issuedAtInstant);

        Instant expiresAtInstant = issuedAtInstant.plus(1, ChronoUnit.HOURS);
        Date expiresAt = Date.from(expiresAtInstant);

        LocalDate currentDate = LocalDate.now();
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public String extractUsername(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().toInstant().isBefore(Instant.now());
    }

}
