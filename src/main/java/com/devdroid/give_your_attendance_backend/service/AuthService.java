package com.devdroid.give_your_attendance_backend.service;

import com.devdroid.give_your_attendance_backend.dto.*;
import com.devdroid.give_your_attendance_backend.entity.User;
import com.devdroid.give_your_attendance_backend.repository.UserRepository;
import com.devdroid.give_your_attendance_backend.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse signup(SignupRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setEmployeeId(request.getEmployeeId());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.EMPLOYEE);
        user.setWorkMode(User.WorkMode.ONSITE);

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(savedUser.getUserId(), savedUser.getEmail());

        return new AuthResponse(
                token,
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getWorkMode().name()
        );
    }

    public AuthResponse login(LoginRequest request) {
        // Find user by email or employee ID
        User user = userRepository.findByEmail(request.getEmailOrEmployeeId())
                .orElseGet(() -> userRepository.findByEmployeeId(request.getEmailOrEmployeeId())
                        .orElseThrow(() -> new RuntimeException("Invalid credentials")));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmail());

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getWorkMode().name()
        );
    }
}

