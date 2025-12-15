package com.devdroid.give_your_attendance_backend.service;

import org.springframework.stereotype.Service;
import com.devdroid.give_your_attendance_backend.entity.LeaveApplication;
import com.devdroid.give_your_attendance_backend.repository.LeaveApplicationRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class LeaveService {

    private final LeaveApplicationRepository repository;

    public LeaveService(LeaveApplicationRepository repository) {
        this.repository = repository;
    }

    public LeaveApplication applyLeave(Long userId, LeaveApplication leave) {
        leave.setUserId(userId);
        return repository.save(leave);
    }

    public List<LeaveApplication> getLeavesByUser(Long userId) {
        return repository.findAll().stream().filter(l -> l.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public LeaveApplication updateStatus(Long id, LeaveApplication updatedLeave) {
        LeaveApplication leave = repository.findById(id).orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus(updatedLeave.getStatus());
        leave.setApprovedAt(LocalDateTime.now());
        leave.setAdminComment(updatedLeave.getAdminComment());
        return repository.save(leave);
    }

    public List<LeaveApplication> getAllLeaves() {
        return repository.findAll();
    }
}
