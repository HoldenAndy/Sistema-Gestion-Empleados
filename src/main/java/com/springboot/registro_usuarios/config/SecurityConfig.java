package com.springboot.registro_usuarios.config;

import com.springboot.registro_usuarios.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration/*
Esta notación le dice a Spring que esta clase contiene métodos que definen beans de configuración.
Esto se usa especialmente cuando necesitamos que un método sea seguido por Spring Framework,
ya que es de alguna clase externa a la de nuestro proyecto, por lo que no es un componente de Spring.
*/

@EnableWebSecurity/*
Activa la configuración de seguridad web de Spring.
*/

public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }/*
    @Bean: Anotación que se usa en los métodos para indicar que el objeto que devuelve
    debe ser gestionado por el Contenedor de Spring.
    En este caso, el método passwordEncoder() crea una instancia de BCryptPasswordEncoder
    y se la pasa a Spring para que la guarde como un bean (PasswordEncoder).
    */

}
