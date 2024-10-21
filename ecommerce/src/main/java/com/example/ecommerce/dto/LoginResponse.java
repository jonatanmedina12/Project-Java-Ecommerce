package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Rol;

public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Rol role;
    private Boolean activo_login;

    // Constructor existente
    public LoginResponse(String token, Long id, String username, String email, Rol role1, Boolean activoLogin) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role1;
        activo_login = activoLogin;
    }


    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    public Rol getRole() {
        return role;
    }

    public void setRole(Rol role) {
        this.role = role;
    }

    public Boolean getActivo_login() {
        return activo_login;
    }

    public void setActivo_login(Boolean activo_login) {
        this.activo_login = activo_login;
    }
}
