package com.devdroid.give_your_attendance_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email or Employee ID is required")
    private String emailOrEmployeeId;

    @NotBlank(message = "Password is required")
    private String password;
}

