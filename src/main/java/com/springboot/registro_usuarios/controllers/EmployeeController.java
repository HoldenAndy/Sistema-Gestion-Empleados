package com.springboot.registro_usuarios.controllers;

import com.springboot.registro_usuarios.dto.EmployeeCreationRequest;
import com.springboot.registro_usuarios.models.Employee;
import com.springboot.registro_usuarios.services.EmployeeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(){
        List<Employee> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeCreationRequest employeeCreationRequest){
        Employee newEmployee = employeeService.createEmployee(employeeCreationRequest);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

}
