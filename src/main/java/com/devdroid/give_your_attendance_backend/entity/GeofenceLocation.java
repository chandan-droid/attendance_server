package com.devdroid.give_your_attendance_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "geofence_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeofenceLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geofence_id")
    private Long geofenceId;

    @Column(name = "location_name", nullable = false, length = 100)
    private String locationName;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "radius_meters")
    private Integer radiusMeters = 200;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

