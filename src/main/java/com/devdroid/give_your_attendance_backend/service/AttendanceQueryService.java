package com.devdroid.give_your_attendance_backend.service;

import org.springframework.stereotype.Service;
import com.devdroid.give_your_attendance_backend.entity.AttendanceQuery;
import com.devdroid.give_your_attendance_backend.repository.AttendanceQueryRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class AttendanceQueryService {

    private final AttendanceQueryRepository repository;

    public AttendanceQueryService(AttendanceQueryRepository repository) {
        this.repository = repository;
    }

    public AttendanceQuery createQuery(Long userId, AttendanceQuery query) {
        query.setUserId(userId);
        return repository.save(query);
    }

    public List<AttendanceQuery> getQueriesByUser(Long userId) {
        return repository.findAll().stream().filter(q -> q.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public AttendanceQuery updateStatus(Long id, AttendanceQuery updatedQuery) {
        AttendanceQuery query = repository.findById(id).orElseThrow(() -> new RuntimeException("Query not found"));
        query.setStatus(updatedQuery.getStatus());
        query.setResolvedAt(LocalDateTime.now());
        query.setAdminComment(updatedQuery.getAdminComment());
        return repository.save(query);
    }
}
