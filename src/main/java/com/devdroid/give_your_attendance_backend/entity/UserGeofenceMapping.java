package com.devdroid.give_your_attendance_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_geofence_mapping",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "geofence_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGeofenceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Long mappingId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "geofence_id", nullable = false)
    private Long geofenceId;
}
