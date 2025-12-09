package com.devdroid.give_your_attendance_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkingHoursResponse {
    private Long projectId;
    private String projectName;
    private Long totalMinutes;
    private Double totalHours;
    private String formattedTime; // e.g., "8h 30m"

    public WorkingHoursResponse(Long totalMinutes) {
        this.totalMinutes = totalMinutes;
        this.totalHours = totalMinutes / 60.0;
        this.formattedTime = formatMinutes(totalMinutes);
    }

    public WorkingHoursResponse(Long projectId, String projectName, Long totalMinutes) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.totalMinutes = totalMinutes;
        this.totalHours = totalMinutes / 60.0;
        this.formattedTime = formatMinutes(totalMinutes);
    }

    private String formatMinutes(Long minutes) {
        if (minutes == null || minutes == 0) {
            return "0h 0m";
        }
        long hours = minutes / 60;
        long mins = minutes % 60;
        return hours + "h " + mins + "m";
    }
}

