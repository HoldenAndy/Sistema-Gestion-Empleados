package com.springboot.registro_usuarios.controllers;

import com.springboot.registro_usuarios.dto.LoginRequest;
import com.springboot.registro_usuarios.models.User;
import com.springboot.registro_usuarios.services.AuthService;
import com.springboot.registro_usuarios.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser (@RequestBody User user/*
    @RequestBody User user: Esta anotación le dice a Spring que tome el cuerpo de la petición HTTP
    (el JSON que envía el cliente) y lo convierta automáticamente en un objeto User de Java.
    Esto es uno de los mayores beneficios de Spring.
     */
    ){

        User registeredUser = authService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);/*
        Un ResponseEntity es una clase de Spring que representa la respuesta HTTP completa,
        incluyendo el cuerpo, los encabezados y el código de estado. Aquí, estamos devolviendo
        el usuario registrado y un código de estado 201 Created, que es la respuesta estándar
        para la creación exitosa de un recurso.
        */
    }

    @PostMapping("/login")
    /*
    public ResponseEntity<String> loginUser (@RequestBody LoginRequest loginRequest){
        boolean isAuthenticated = authService.validateLogin(loginRequest);

        if(isAuthenticated == false){
            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.OK);
        }
        return new ResponseEntity<>("Login éxitoso", HttpStatus.OK);
    }
    */
    public ResponseEntity<?> loginUser (@RequestBody LoginRequest loginRequest){
        User authenticatedUser = authService.validateLogin(loginRequest);

        if(authenticatedUser != null){
            String token = jwtService.generatedToken(authenticatedUser);
            return new ResponseEntity<>(Collections.singletonMap("token", token), HttpStatus.OK);
        }
        return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/user/data")
    public ResponseEntity<String> getUserData() {
        return new ResponseEntity<>("¡Acceso exitoso a datos protegidos!", HttpStatus.OK);
    }

}
