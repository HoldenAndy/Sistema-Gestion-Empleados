package com.springboot.registro_usuarios.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        String errorMsg = ex.getMessage();

        if (errorMsg != null && errorMsg.contains("Cannot deserialize value of type") && errorMsg.contains("Status")) {
            return new ResponseEntity<>("Error de formato: El valor del estado de la tarea no es válido. Los valores aceptados son: PENDING o COMPLETED.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Error en el formato de la solicitud JSON. Verifique la estructura de los datos enviados.", HttpStatus.BAD_REQUEST);
    }
}