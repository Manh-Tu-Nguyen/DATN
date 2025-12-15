package com.example.shoes_store_backend.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class LoginResponse {
    private Integer id;
    private String ten;
    private String role;

    public LoginResponse(Integer id, String ten, String role) {
        this.id = id;
        this.ten = ten;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

    public String getRole() {
        return role;
    }
}
