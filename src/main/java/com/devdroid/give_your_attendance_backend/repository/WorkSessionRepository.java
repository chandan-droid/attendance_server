package com.devdroid.give_your_attendance_backend.repository;

import com.devdroid.give_your_attendance_backend.entity.WorkSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
    List<WorkSession> findByUserId(Long userId);

    @Query("SELECT ws FROM WorkSession ws WHERE ws.userId = :userId AND ws.projectId = :projectId")
    List<WorkSession> findByUserIdAndProjectId(Long userId, Long projectId);

    @Query("SELECT ws FROM WorkSession ws WHERE ws.userId = :userId AND ws.taskId = :taskId")
    List<WorkSession> findByUserIdAndTaskId(Long userId, Long taskId);

    @Query("SELECT COALESCE(SUM(ws.durationMinutes), 0) FROM WorkSession ws " +
           "JOIN Attendance a ON ws.punchInId = a.attendanceId " +
           "WHERE ws.userId = :userId AND ws.projectId = :projectId")
    Long getTotalMinutesForUserAndProject(@Param("userId") Long userId, @Param("projectId") Long projectId);

    @Query("SELECT COALESCE(SUM(ws.durationMinutes), 0) FROM WorkSession ws " +
           "JOIN Attendance a ON ws.punchInId = a.attendanceId " +
           "WHERE ws.userId = :userId " +
           "AND DATE(a.punchTime) = :date")
    Long getTotalMinutesForUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
