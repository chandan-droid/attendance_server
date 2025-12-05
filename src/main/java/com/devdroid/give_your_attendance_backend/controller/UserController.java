package com.devdroid.give_your_attendance_backend.controller;

import com.devdroid.give_your_attendance_backend.dto.ApiResponse;
import com.devdroid.give_your_attendance_backend.entity.User;
import com.devdroid.give_your_attendance_backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Remove password hash before sending
            user.setPasswordHash(null);

            return ResponseEntity.ok(new ApiResponse(true, "User profile retrieved", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            // Remove password hashes
            users.forEach(user -> user.setPasswordHash(null));
            return ResponseEntity.ok(new ApiResponse(true, "Users retrieved", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{userId}/work-mode")
    public ResponseEntity<?> updateWorkMode(@PathVariable Long userId,
                                           @RequestParam String workMode) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setWorkMode(User.WorkMode.valueOf(workMode.toUpperCase()));
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true, "Work mode updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
