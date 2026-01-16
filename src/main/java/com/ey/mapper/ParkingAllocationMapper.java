package com.ey.mapper;

import com.ey.dto.response.ParkingAllocationResponseDTO;
import com.ey.entity.ParkingAllocation;

public class ParkingAllocationMapper {

    public static ParkingAllocationResponseDTO toResponse(ParkingAllocation allocation) {

        ParkingAllocationResponseDTO dto = new ParkingAllocationResponseDTO();
        dto.setAllocationId(allocation.getAllocationId());
        dto.setCustomerId(allocation.getCustomer().getUserId());
        dto.setVehicleId(allocation.getVehicle().getVehicleId());
        dto.setSlotId(allocation.getSlot().getSlotId());
        dto.setStartTime(allocation.getStartTime());
        dto.setEndTime(allocation.getEndTime());
        dto.setStatus(allocation.getStatus());

        return dto;
    }
}