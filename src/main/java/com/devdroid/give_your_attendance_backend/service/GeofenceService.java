package com.devdroid.give_your_attendance_backend.service;

import com.devdroid.give_your_attendance_backend.entity.GeofenceLocation;
import com.devdroid.give_your_attendance_backend.entity.UserGeofenceMapping;
import com.devdroid.give_your_attendance_backend.repository.GeofenceLocationRepository;
import com.devdroid.give_your_attendance_backend.repository.UserGeofenceMappingRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class GeofenceService {

    private final GeofenceLocationRepository geofenceLocationRepository;
    private final UserGeofenceMappingRepository userGeofenceMappingRepository;

    public GeofenceService(GeofenceLocationRepository geofenceLocationRepository,
                           UserGeofenceMappingRepository userGeofenceMappingRepository) {
        this.geofenceLocationRepository = geofenceLocationRepository;
        this.userGeofenceMappingRepository = userGeofenceMappingRepository;
    }

    /**
     * Check if user is within any of their assigned geofence locations
     */
    public boolean isWithinGeofence(Long userId, BigDecimal latitude, BigDecimal longitude) {
        List<UserGeofenceMapping> mappings = userGeofenceMappingRepository.findByUserId(userId);

        for (UserGeofenceMapping mapping : mappings) {
            GeofenceLocation geofence = geofenceLocationRepository.findById(mapping.getGeofenceId())
                    .orElse(null);

            if (geofence != null) {
                double distance = calculateDistance(
                        latitude.doubleValue(),
                        longitude.doubleValue(),
                        geofence.getLatitude().doubleValue(),
                        geofence.getLongitude().doubleValue()
                );

                if (distance <= geofence.getRadiusMeters()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Calculate distance between two points using Haversine formula
     * Returns distance in meters
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public GeofenceLocation createGeofence(GeofenceLocation geofence) {
        return geofenceLocationRepository.save(geofence);
    }

    public void assignGeofenceToUser(Long userId, Long geofenceId) {
        UserGeofenceMapping mapping = new UserGeofenceMapping();
        mapping.setUserId(userId);
        mapping.setGeofenceId(geofenceId);
        userGeofenceMappingRepository.save(mapping);
    }

    public List<GeofenceLocation> getAllGeofences() {
        return geofenceLocationRepository.findAll();
    }
}

