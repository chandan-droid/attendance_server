package com.devdroid.give_your_attendance_backend.repository;

import com.devdroid.give_your_attendance_backend.entity.GeofenceLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeofenceLocationRepository extends JpaRepository<GeofenceLocation, Long> {
}

