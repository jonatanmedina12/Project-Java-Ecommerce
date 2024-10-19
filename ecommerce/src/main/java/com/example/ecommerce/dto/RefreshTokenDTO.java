package com.example.ecommerce.dto;

public class RefreshTokenDTO {

    private String Username;
    private String Token;

    public RefreshTokenDTO(String token,String username) {
        Token = token;
        Username=username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
