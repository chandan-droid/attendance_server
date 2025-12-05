package com.devdroid.give_your_attendance_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PunchResponse {
    private Long attendanceId;
    private String punchType;
    private LocalDateTime punchTime;
    private String message;
    private Long workSessionId;
}

