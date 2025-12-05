package com.devdroid.give_your_attendance_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "task_name", nullable = false, length = 150)
    private String taskName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "estimated_hours", precision = 6, scale = 2)
    private BigDecimal estimatedHours;
}

