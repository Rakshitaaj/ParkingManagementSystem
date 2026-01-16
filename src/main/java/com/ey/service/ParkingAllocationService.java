package com.ey.service;

import com.ey.entity.ParkingAllocation;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingAllocationService {

    ParkingAllocation allocateSlot(
            Long customerId,
            Long vehicleId,
            Long slotId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    List<ParkingAllocation> getAllocationsByCustomer(Long customerId);

    List<ParkingAllocation> getAllocationsBySlot(Long slotId);

    ParkingAllocation cancelAllocation(Long allocationId);
}
