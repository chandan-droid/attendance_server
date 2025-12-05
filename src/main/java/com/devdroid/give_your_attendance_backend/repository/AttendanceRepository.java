package com.devdroid.give_your_attendance_backend.repository;

import com.devdroid.give_your_attendance_backend.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserIdOrderByPunchTimeDesc(Long userId);

    @Query("SELECT a FROM Attendance a WHERE a.userId = :userId AND a.punchType = 'IN' AND a.attendanceId NOT IN (SELECT ws.punchInId FROM WorkSession ws WHERE ws.userId = :userId AND ws.punchInId IS NOT NULL)")
    Optional<Attendance> findLatestUnmatchedPunchIn(Long userId);

    List<Attendance> findByUserIdAndPunchTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
}

