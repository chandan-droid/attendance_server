package com.devdroid.give_your_attendance_backend.repository;

import com.devdroid.give_your_attendance_backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
}

