package com.ey.repository;

import com.ey.entity.ParkingLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingLocationRepository extends JpaRepository<ParkingLocation, Long> {

    List<ParkingLocation> findByCity(String city);
    List<ParkingLocation> findByParkingProviderUserId(Long providerId);
}
