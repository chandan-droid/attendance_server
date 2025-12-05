package com.devdroid.give_your_attendance_backend.repository;

import com.devdroid.give_your_attendance_backend.entity.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    List<ProjectAssignment> findByUserId(Long userId);
    List<ProjectAssignment> findByProjectId(Long projectId);
}

