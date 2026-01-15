package com.ey.repository;

import com.ey.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByLocationLocationId(Long locationId);

    List<ParkingSlot> findByActiveTrue();
}
