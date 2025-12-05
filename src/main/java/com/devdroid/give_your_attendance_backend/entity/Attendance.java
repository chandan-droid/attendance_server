package com.devdroid.give_your_attendance_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "task_id")
    private Long taskId;

    @Enumerated(EnumType.STRING)
    @Column(name = "punch_type", nullable = false)
    private PunchType punchType;

    @Column(name = "punch_time")
    private LocalDateTime punchTime;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @PrePersist
    protected void onCreate() {
        if (punchTime == null) {
            punchTime = LocalDateTime.now();
        }
    }

    public enum PunchType {
        IN, OUT
    }
}

