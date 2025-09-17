package com.springboot.registro_usuarios.services;

import com.springboot.registro_usuarios.dto.EmployeeCreationRequest;
import com.springboot.registro_usuarios.dto.EmployeeUpdateRequest;
import com.springboot.registro_usuarios.dto.TaskCreationRequest;
import com.springboot.registro_usuarios.dto.TaskUpdateRequest;
import com.springboot.registro_usuarios.models.Employee;
import com.springboot.registro_usuarios.models.Role;
import com.springboot.registro_usuarios.models.Task;
import com.springboot.registro_usuarios.models.User;
import com.springboot.registro_usuarios.repositories.EmployeeRepository;
import com.springboot.registro_usuarios.repositories.TaskRepository;
import com.springboot.registro_usuarios.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    public final EmployeeRepository employeeRepository;
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final TaskRepository taskRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TaskRepository taskRepository){
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
    }

    public List<Employee> findAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee createEmployee(EmployeeCreationRequest employeeCreationRequest) {
        if (userRepository.findByEmail(employeeCreationRequest.getEmail()).isPresent()) {
            throw new IllegalStateException("El email ya existe");
        }
        User newUser = new User();
        newUser.setEmail(employeeCreationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(employeeCreationRequest.getPassword()));
        newUser.setRole(Role.EMPLOYEE);
        newUser.setFirstLogin(true);
        User savedUser = userRepository.save(newUser);

        Employee newEmployee = new Employee();
        newEmployee.setName(employeeCreationRequest.getName());
        newEmployee.setLastName(employeeCreationRequest.getLastName());
        newEmployee.setWorkStation(employeeCreationRequest.getWorkStation());
        newEmployee.setPhoneNumber(employeeCreationRequest.getPhoneNumber());
        newEmployee.setDate(employeeCreationRequest.getDate());
        newEmployee.setUser(savedUser);

        return employeeRepository.save(newEmployee);
    }

    public Employee updateEmployee(Long id, EmployeeUpdateRequest request) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(request.getName());
                    employee.setLastName(request.getLastName());
                    employee.setWorkStation(request.getWorkStation());
                    employee.setPhoneNumber(request.getPhoneNumber());
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new IllegalStateException("Trabajador con ID " + id + " no encontrado."));
    }

    @Transactional
    public void deleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Trabajador con ID " + id + " no encontrado."));

        userRepository.deleteById(employee.getUser().getId());

        employeeRepository.delete(employee);
    }

    public Optional<Task> assignTask(TaskCreationRequest request) {
        Optional<Employee> employeeOpt = employeeRepository.findById(request.getEmployeeId());

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            Task task = new Task();
            task.setDescription(request.getDescription());
            task.setDueDate(request.getDueDate());
            task.setStatus("PENDING");
            task.setEmployee(employee);
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }

    public Optional<Task> updateTaskStatus(Long taskId, TaskUpdateRequest request) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setStatus(request.getStatus());

            if ("COMPLETED".equalsIgnoreCase(request.getStatus())) {
                Employee employee = task.getEmployee();
                double currentScore = employee.getPerformanceScore();
                employee.setPerformanceScore(currentScore + 10.0);
                employeeRepository.save(employee);
            }

            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }

    public List<Task> findTasksByEmployeeId(Long employeeId) {
        return taskRepository.findByEmployeeId(employeeId);
    }
}