package com.devdroid.give_your_attendance_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.devdroid.give_your_attendance_backend.service.LeaveService;
import com.devdroid.give_your_attendance_backend.entity.LeaveApplication;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(@RequestBody LeaveApplication leave, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(leaveService.applyLeave(userId, leave));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyLeaves(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(leaveService.getLeavesByUser(userId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLeave(@PathVariable Long id, @RequestBody LeaveApplication leave) {
        return ResponseEntity.ok(leaveService.updateStatus(id, leave));
    }
}
