package com.devdroid.give_your_attendance_backend.service;

import com.devdroid.give_your_attendance_backend.dto.PunchRequest;
import com.devdroid.give_your_attendance_backend.dto.PunchResponse;
import com.devdroid.give_your_attendance_backend.entity.Attendance;
import com.devdroid.give_your_attendance_backend.entity.User;
import com.devdroid.give_your_attendance_backend.entity.WorkSession;
import com.devdroid.give_your_attendance_backend.repository.AttendanceRepository;
import com.devdroid.give_your_attendance_backend.repository.UserRepository;
import com.devdroid.give_your_attendance_backend.repository.WorkSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final WorkSessionRepository workSessionRepository;
    private final GeofenceService geofenceService;

    public AttendanceService(AttendanceRepository attendanceRepository,
                           UserRepository userRepository,
                           WorkSessionRepository workSessionRepository,
                           GeofenceService geofenceService) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.workSessionRepository = workSessionRepository;
        this.geofenceService = geofenceService;
    }

    @Transactional
    public PunchResponse punchIn(Long userId, PunchRequest request) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is ONSITE and validate geofence
        if (user.getWorkMode() == User.WorkMode.ONSITE) {
            boolean withinGeofence = geofenceService.isWithinGeofence(
                    userId,
                    request.getLatitude(),
                    request.getLongitude()
            );

            if (!withinGeofence) {
                throw new RuntimeException("You are not within the allowed geofence location for punch in");
            }
        }

        // Validate project and task are provided
        if (request.getProjectId() == null || request.getTaskId() == null) {
            throw new RuntimeException("Project and Task must be selected for punch in");
        }

        // Create attendance record
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setProjectId(request.getProjectId());
        attendance.setTaskId(request.getTaskId());
        attendance.setPunchType(Attendance.PunchType.IN);
        attendance.setLatitude(request.getLatitude());
        attendance.setLongitude(request.getLongitude());
        attendance.setPunchTime(LocalDateTime.now());

        Attendance savedAttendance = attendanceRepository.save(attendance);

        return new PunchResponse(
                savedAttendance.getAttendanceId(),
                savedAttendance.getPunchType().name(),
                savedAttendance.getPunchTime(),
                "Punch in successful",
                null
        );
    }

    @Transactional
    public PunchResponse punchOut(Long userId, PunchRequest request) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is ONSITE and validate geofence
        if (user.getWorkMode() == User.WorkMode.ONSITE) {
            boolean withinGeofence = geofenceService.isWithinGeofence(
                    userId,
                    request.getLatitude(),
                    request.getLongitude()
            );

            if (!withinGeofence) {
                throw new RuntimeException("You are not within the allowed geofence location for punch out");
            }
        }

        // Find the latest unmatched punch in
        Optional<Attendance> punchInOpt = attendanceRepository.findLatestUnmatchedPunchIn(userId);

        if (punchInOpt.isEmpty()) {
            throw new RuntimeException("No active punch in found. Please punch in first");
        }

        Attendance punchIn = punchInOpt.get();

        // Create punch out record
        Attendance punchOut = new Attendance();
        punchOut.setUserId(userId);
        punchOut.setProjectId(punchIn.getProjectId());
        punchOut.setTaskId(punchIn.getTaskId());
        punchOut.setPunchType(Attendance.PunchType.OUT);
        punchOut.setLatitude(request.getLatitude());
        punchOut.setLongitude(request.getLongitude());
        punchOut.setPunchTime(LocalDateTime.now());

        Attendance savedPunchOut = attendanceRepository.save(punchOut);

        // Create work session
        WorkSession workSession = new WorkSession();
        workSession.setUserId(userId);
        workSession.setProjectId(punchIn.getProjectId());
        workSession.setTaskId(punchIn.getTaskId());
        workSession.setPunchInId(punchIn.getAttendanceId());
        workSession.setPunchOutId(savedPunchOut.getAttendanceId());

        // Calculate duration in minutes
        long minutes = Duration.between(punchIn.getPunchTime(), savedPunchOut.getPunchTime()).toMinutes();
        workSession.setDurationMinutes((int) minutes);

        WorkSession savedSession = workSessionRepository.save(workSession);

        return new PunchResponse(
                savedPunchOut.getAttendanceId(),
                savedPunchOut.getPunchType().name(),
                savedPunchOut.getPunchTime(),
                "Punch out successful. Duration: " + minutes + " minutes",
                savedSession.getSessionId()
        );
    }

    public List<Attendance> getUserAttendanceHistory(Long userId) {
        return attendanceRepository.findByUserIdOrderByPunchTimeDesc(userId);
    }

    public List<WorkSession> getUserWorkSessions(Long userId) {
        return workSessionRepository.findByUserId(userId);
    }
}

