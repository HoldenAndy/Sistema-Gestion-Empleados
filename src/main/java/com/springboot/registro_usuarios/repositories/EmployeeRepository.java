package com.springboot.registro_usuarios.repositories;

import com.springboot.registro_usuarios.models.Employee;
import com.springboot.registro_usuarios.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}