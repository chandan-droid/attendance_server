// java
package com.devdroid.give_your_attendance_backend.repository;

import com.devdroid.give_your_attendance_backend.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query(value = "SELECT a.* FROM attendance a " +
            "WHERE a.user_id = :userId " +
            "  AND a.punch_type = 'IN' " +
            "  AND a.attendance_id NOT IN (SELECT ws.punch_in_id FROM work_sessions ws WHERE ws.punch_in_id IS NOT NULL) " +
            "ORDER BY a.punch_time DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Attendance> findLatestUnmatchedPunchIn(@Param("userId") Long userId);

    @Query(value = "SELECT a.* FROM attendance a " +
            "WHERE a.user_id = :userId " +
            "  AND a.punch_type = 'OUT' " +
            "  AND a.attendance_id NOT IN (SELECT ws.punch_out_id FROM work_sessions ws WHERE ws.punch_out_id IS NOT NULL) " +
            "ORDER BY a.punch_time DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Attendance> findLatestUnmatchedPunchOut(@Param("userId") Long userId);

    List<Attendance> findByUserIdOrderByPunchTimeDesc(Long userId);
}
