package com.springboot.registro_usuarios.models;

import jakarta.persistence.*;

@Entity

@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean firstLogin = true;

    public User() {
    }

    public User(Long id, String email, String password, Role role, boolean firstLogin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstLogin = firstLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole(){ return role; }

    public void setRole(Role role) { this.role = role; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstLogin(){
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin){
        this.firstLogin = firstLogin;
    }
}
