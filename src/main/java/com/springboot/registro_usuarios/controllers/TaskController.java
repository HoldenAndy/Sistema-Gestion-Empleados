package com.springboot.registro_usuarios.controllers;

import com.springboot.registro_usuarios.models.dto.TaskCreationRequest;
import com.springboot.registro_usuarios.models.dto.TaskUpdateRequest;
import com.springboot.registro_usuarios.models.entities.Status;
import com.springboot.registro_usuarios.models.entities.Task;
import com.springboot.registro_usuarios.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final EmployeeService employeeService;

    public TaskController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> assignTask(@RequestBody TaskCreationRequest request) {
        if (request.getEmployeeId() == null) {
            return new ResponseEntity<>("El ID del empleado es obligatorio.", HttpStatus.BAD_REQUEST);
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return new ResponseEntity<>("La descripción de la tarea es obligatoria.", HttpStatus.BAD_REQUEST);
        }
        if (request.getDueDate() == null) {
            return new ResponseEntity<>("La fecha límite de la tarea es obligatoria.", HttpStatus.BAD_REQUEST);
        }

        return employeeService.assignTask(request)
                .map(task -> new ResponseEntity<Object>(task, HttpStatus.CREATED))
                .orElse(new ResponseEntity<Object>("Empleado no encontrado con ID: " + request.getEmployeeId(), HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update-status/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Object> updateTaskStatus(@PathVariable Long taskId, @RequestBody TaskUpdateRequest request) {
        if (request.getStatus() == null || request.getStatus().toString().isEmpty()) {
            return new ResponseEntity<>("El estado de la tarea es obligatorio.", HttpStatus.BAD_REQUEST);
        }
        if (!Status.PENDING.equals(request.getStatus()) && !Status.COMPLETED.equals(request.getStatus())) {
            return new ResponseEntity<>("El estado debe ser 'PENDING' o 'COMPLETED'.", HttpStatus.BAD_REQUEST);
        }

        return employeeService.updateTaskStatus(taskId, request)
                .map(task -> new ResponseEntity<Object>(task, HttpStatus.OK))
                .orElse(new ResponseEntity<Object>("Tarea no encontrada con ID: " + taskId, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<Task>> getTasksByEmployeeId(@PathVariable Long employeeId) {
            List<Task> tasks = employeeService.findTasksByEmployeeId(employeeId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}