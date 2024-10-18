package com.example.ecommerce.dto;

public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private String password; // Solo se usa para creación/actualización, nunca se devuelve
    private boolean activo;
    private String rol;
    private Long totalOrdenes; // Para el reporte de clientes frecuentes

    // Constructor por defecto
    public UsuarioDTO() {}

    // Constructor con todos los campos (excepto password y totalOrdenes)
    public UsuarioDTO(Long id, String username, String email, boolean activo, String rol) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.activo = activo;
        this.rol = rol;
    }

    // Getters y setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getTotalOrdenes() {
        return totalOrdenes;
    }

    public void setTotalOrdenes(Long totalOrdenes) {
        this.totalOrdenes = totalOrdenes;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                ", rol='" + rol + '\'' +
                ", totalOrdenes=" + totalOrdenes +
                '}';
    }
}
