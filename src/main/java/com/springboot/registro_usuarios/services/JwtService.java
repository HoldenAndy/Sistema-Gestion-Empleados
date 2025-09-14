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
    private String secretKey;/*
    Esta anotación inyecta el valor de la propiedad jwt.secret de application.properties
    en la variable secretKey.
    */

    public String generatedToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);/*
        Algorithm: Es una clase proporcionada por la librería java-jwt que se encarga de gestionar
        los algoritmos de firma y verificación de tokens.
        Hay varios algoritmos que se pueden usar para firmar un JWT,
        cada uno con diferentes niveles de seguridad.

        HMAC256 Es el algoritmo de firma que estamos seleccionando.
        HMAC significa Hash-based Message Authentication Code,
        y 256 se refiere a la longitud de la clave, en este caso, 256 bits.

        Este algoritmo es un método de cifrado simétrico.
        Esto significa que usa la misma clave secreta (secretKey) tanto para firmar el token
        (es decir, generar la firma) como para verificarlo
        (es decir, asegurarse de que no ha sido modificado).
        */

        // Instante de creación del token (ahora)
        Instant issuedAtInstant = Instant.now();
        Date issuedAt = Date.from(issuedAtInstant);

        // Instante de expiración del token (1 hora después)
        Instant expiresAtInstant = issuedAtInstant.plus(1, ChronoUnit.HOURS);
        Date expiresAt = Date.from(expiresAtInstant);

        LocalDate currentDate = LocalDate.now();
        return JWT.create()
                .withSubject(user.getEmail())/*
                El "sujeto" del token es el email del usuario.
                Es una forma de identificar a quién pertenece el token.
                */
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)/*
                Establecemos una fecha de expiración para el token (en este caso, 1 hora).
                Es una buena práctica por seguridad.
                */
                .sign(algorithm);/*
                Firma el token usando nuestro algoritmo y clave secreta.
                Esto asegura que el token no pueda ser alterado.
                */
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
