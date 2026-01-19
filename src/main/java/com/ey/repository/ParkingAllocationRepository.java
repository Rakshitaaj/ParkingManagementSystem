package com.ey.repository;

import com.ey.entity.ParkingAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingAllocationRepository extends JpaRepository<ParkingAllocation, Long> {

    List<ParkingAllocation> findByCustomerUserId(Long customerId);

    List<ParkingAllocation> findBySlotSlotId(Long slotId);

    boolean existsBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long slotId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
    
    List<ParkingAllocation>
    findBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long slotId,
            LocalDateTime endTime,
            LocalDateTime startTime);

}
