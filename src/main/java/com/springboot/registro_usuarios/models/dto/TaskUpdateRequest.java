package com.springboot.registro_usuarios.models.dto;

import com.springboot.registro_usuarios.models.entities.Status;

public class TaskUpdateRequest {

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) { this.status = status; }
}