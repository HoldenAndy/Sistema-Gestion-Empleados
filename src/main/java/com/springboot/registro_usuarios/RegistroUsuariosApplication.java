package com.springboot.registro_usuarios;

import com.springboot.registro_usuarios.models.Employee;
import com.springboot.registro_usuarios.models.Role;
import com.springboot.registro_usuarios.models.User;
import com.springboot.registro_usuarios.repositories.UserRepository;
import com.springboot.registro_usuarios.repositories.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class RegistroUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistroUsuariosApplication.class, args);
	}

	@Bean
	public CommandLineRunner createAdminUser(UserRepository userRepository,
											 EmployeeRepository employeeRepository,
											 PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByEmail("admin@empresa.com").isEmpty()) {
				User adminUser = new User();
				adminUser.setEmail("admin@empresa.com");
				adminUser.setPassword(passwordEncoder.encode("admin123"));
				adminUser.setRole(Role.ADMIN);
				adminUser.setFirstLogin(false);
				userRepository.save(adminUser);

				Employee adminEmployee = new Employee();
				adminEmployee.setName("Andrés Alonso");
				adminEmployee.setLastName("Carrasco Martínez");
				adminEmployee.setWorkStation("RRHH");
				adminEmployee.setPhoneNumber(951570191);
				adminEmployee.setDate(LocalDate.now());
				adminEmployee.setUser(adminUser);

				employeeRepository.save(adminEmployee);

				System.out.println("✅ Primer usuario administrador creado.");
				System.out.println("✅ Primer trabajador administrador creado.");
			} else {
				System.out.println("❌ El usuario administrador ya existe.");
				System.out.println("❌ El trabajador administrador ya existe");
			}
		};
	}

}
