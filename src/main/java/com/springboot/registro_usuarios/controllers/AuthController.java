package com.springboot.registro_usuarios.controllers;

import com.springboot.registro_usuarios.models.dto.LoginRequest;
import com.springboot.registro_usuarios.models.dto.PasswordChangeRequest;
import com.springboot.registro_usuarios.models.entities.User;
import com.springboot.registro_usuarios.services.AuthService;
import com.springboot.registro_usuarios.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser (@RequestBody LoginRequest loginRequest){
        User user = authService.validateLogin(loginRequest);

        if(user != null){
            String token = jwtService.generatedToken(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("firstLogin", user.isFirstLogin());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/user/data")
    public ResponseEntity<String> getUserData() {
        return new ResponseEntity<>("¡Acceso exitoso a datos protegidos!", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.changePassword(userEmail, request.getNewPassword());
        return new ResponseEntity<>("Contraseña actualizada con éxito.", HttpStatus.OK);
    }

}
