package com.devdroid.give_your_attendance_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Password is required")
    private String password;

    private String role; // EMPLOYEE or ADMIN (optional, defaults to EMPLOYEE)

    private String workMode; // ONSITE or REMOTE (optional, defaults to ONSITE)
}
