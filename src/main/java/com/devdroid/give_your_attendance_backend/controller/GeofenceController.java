package com.devdroid.give_your_attendance_backend.controller;

import com.devdroid.give_your_attendance_backend.dto.ApiResponse;
import com.devdroid.give_your_attendance_backend.entity.GeofenceLocation;
import com.devdroid.give_your_attendance_backend.service.GeofenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/geofence")
@CrossOrigin(origins = "*")
public class GeofenceController {

    private final GeofenceService geofenceService;

    public GeofenceController(GeofenceService geofenceService) {
        this.geofenceService = geofenceService;
    }

    @PostMapping
    public ResponseEntity<?> createGeofence(@RequestBody GeofenceLocation geofence) {
        try {
            GeofenceLocation created = geofenceService.createGeofence(geofence);
            return ResponseEntity.ok(new ApiResponse(true, "Geofence created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGeofences() {
        try {
            List<GeofenceLocation> geofences = geofenceService.getAllGeofences();
            return ResponseEntity.ok(new ApiResponse(true, "Geofences retrieved", geofences));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }


}

