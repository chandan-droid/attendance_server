package com.devdroid.give_your_attendance_backend.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "attendance_queries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queryId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate queryDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    private String adminComment;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}
