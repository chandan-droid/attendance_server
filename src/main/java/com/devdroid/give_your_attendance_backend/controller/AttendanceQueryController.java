package com.devdroid.give_your_attendance_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.devdroid.give_your_attendance_backend.service.AttendanceQueryService;
import com.devdroid.give_your_attendance_backend.entity.AttendanceQuery;

@RestController
@RequestMapping("/api/attendance/query")
@CrossOrigin(origins = "*")
public class AttendanceQueryController {

    private final AttendanceQueryService queryService;

    public AttendanceQueryController(AttendanceQueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<?> raiseQuery(@RequestBody AttendanceQuery query, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(queryService.createQuery(userId, query));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyQueries(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(queryService.getQueriesByUser(userId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveQuery(@PathVariable Long id, @RequestBody AttendanceQuery query) {
        return ResponseEntity.ok(queryService.updateStatus(id, query));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllQueries() {
        return ResponseEntity.ok(queryService.getAllQueries());
    }
}
