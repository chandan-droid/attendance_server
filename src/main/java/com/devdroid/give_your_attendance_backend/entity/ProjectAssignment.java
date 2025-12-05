package com.devdroid.give_your_attendance_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "project_assignments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @PrePersist
    protected void onCreate() {
        if (assignedDate == null) {
            assignedDate = LocalDate.now();
        }
    }
}

