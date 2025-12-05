package com.devdroid.give_your_attendance_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PunchRequest {
    @NotNull(message = "Punch type is required")
    private String punchType; // IN or OUT

    private Long projectId;
    private Long taskId;

    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;
}

