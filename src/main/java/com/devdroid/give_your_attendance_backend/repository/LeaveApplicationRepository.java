package com.devdroid.give_your_attendance_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devdroid.give_your_attendance_backend.entity.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {}
