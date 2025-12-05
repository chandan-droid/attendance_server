package com.devdroid.give_your_attendance_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "work_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "punch_in_id")
    private Long punchInId;

    @Column(name = "punch_out_id")
    private Long punchOutId;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;
}

