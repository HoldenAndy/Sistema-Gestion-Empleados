package com.springboot.registro_usuarios.services;

import com.springboot.registro_usuarios.dto.EmployeeCreationRequest;
import com.springboot.registro_usuarios.dto.EmployeeUpdateRequest;
import com.springboot.registro_usuarios.models.Employee;
import com.springboot.registro_usuarios.models.Role;
import com.springboot.registro_usuarios.models.User;
import com.springboot.registro_usuarios.repositories.EmployeeRepository;
import com.springboot.registro_usuarios.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    public final EmployeeRepository employeeRepository;
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder){
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> findAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee createEmployee(EmployeeCreationRequest employeeCreationRequest) {
        if (userRepository.findByEmail(employeeCreationRequest.getEmail()).isPresent()) {
            throw new IllegalStateException("El email ya está en uso. No se puede crear un nuevo trabajador");
        } else {
            User newUser = new User();
            newUser.setEmail(employeeCreationRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(employeeCreationRequest.getPassword()));
            newUser.setRole(Role.EMPLOYEE);

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

}
