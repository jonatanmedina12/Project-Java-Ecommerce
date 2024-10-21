package com.example.ecommerce.dto;

import org.springframework.web.multipart.MultipartFile;

public class UsuarioUpdateDto {
    private String username;
    private String email;
    private MultipartFile photo;

    public UsuarioUpdateDto(String username, String email,  MultipartFile photo) {
        this.username = username;
        this.email = email;
        this.photo = photo;
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


    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
