package com.devdroid.give_your_attendance_backend.repository;

import com.devdroid.give_your_attendance_backend.entity.UserGeofenceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserGeofenceMappingRepository extends JpaRepository<UserGeofenceMapping, Long> {
    List<UserGeofenceMapping> findByUserId(Long userId);
}
