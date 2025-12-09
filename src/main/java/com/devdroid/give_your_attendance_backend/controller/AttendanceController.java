package com.devdroid.give_your_attendance_backend.controller;

import com.devdroid.give_your_attendance_backend.dto.ApiResponse;
import com.devdroid.give_your_attendance_backend.dto.PunchRequest;
import com.devdroid.give_your_attendance_backend.dto.PunchResponse;
import com.devdroid.give_your_attendance_backend.dto.WorkingHoursResponse;
import com.devdroid.give_your_attendance_backend.entity.Attendance;
import com.devdroid.give_your_attendance_backend.entity.WorkSession;
import com.devdroid.give_your_attendance_backend.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/punch-in")
    public ResponseEntity<?> punchIn(@Valid @RequestBody PunchRequest request,
                                     Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            PunchResponse response = attendanceService.punchIn(userId, request);
            return ResponseEntity.ok(new ApiResponse(true, "Punch in successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/punch-out")
    public ResponseEntity<?> punchOut(@Valid @RequestBody PunchRequest request,
                                      Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            PunchResponse response = attendanceService.punchOut(userId, request);
            return ResponseEntity.ok(new ApiResponse(true, "Punch out successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getAttendanceHistory(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            List<Attendance> history = attendanceService.getUserAttendanceHistory(userId);
            return ResponseEntity.ok(new ApiResponse(true, "Attendance history retrieved", history));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/work-sessions")
    public ResponseEntity<?> getWorkSessions(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            List<WorkSession> sessions = attendanceService.getUserWorkSessions(userId);
            return ResponseEntity.ok(new ApiResponse(true, "Work sessions retrieved", sessions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    /**
     * Get total working hours for a specific project.
     *
     * @param projectId the project ID
     * @param authentication the authenticated user
     * @return total working hours for the project
     */
    @GetMapping("/total-hours/project/{projectId}")
    public ResponseEntity<?> getTotalHoursForProject(@PathVariable Long projectId,
                                                      Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            Long totalMinutes = attendanceService.getTotalWorkingMinutesForProject(userId, projectId);
            WorkingHoursResponse response = new WorkingHoursResponse(totalMinutes);
            return ResponseEntity.ok(new ApiResponse(true, "Total working hours retrieved", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    /**
     * Get total working hours for a specific day.
     *
     * @param date the date (optional, defaults to today if not provided)
     * @param authentication the authenticated user
     * @return total working hours for the day
     */
    @GetMapping("/total-hours/day")
    public ResponseEntity<?> getTotalHoursForDay(@RequestParam(required = false) String date,
                                                  Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            LocalDate workDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : LocalDate.now();
            Long totalMinutes = attendanceService.getTotalWorkingMinutesForDay(userId, workDate);
            WorkingHoursResponse response = new WorkingHoursResponse(totalMinutes);
            return ResponseEntity.ok(new ApiResponse(true, "Total working hours for " + workDate + " retrieved", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
