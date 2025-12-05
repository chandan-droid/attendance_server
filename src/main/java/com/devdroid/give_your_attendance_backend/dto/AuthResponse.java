package com.devdroid.give_your_attendance_backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String name;
    private String email;
    private String role;
    private String workMode;

    public AuthResponse(String token, Long userId, String name, String email, String role, String workMode) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.workMode = workMode;
    }
}
