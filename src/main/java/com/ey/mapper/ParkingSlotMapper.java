package com.ey.mapper;

import com.ey.dto.request.ParkingSlotRequestDTO;
import com.ey.dto.response.ParkingSlotResponseDTO;
import com.ey.entity.ParkingSlot;

public class ParkingSlotMapper {

    public static ParkingSlot toEntity(ParkingSlotRequestDTO dto) {
        ParkingSlot slot = new ParkingSlot();
        slot.setSlotNumber(dto.getSlotNumber());
        return slot;
    }

    public static ParkingSlotResponseDTO toResponse(ParkingSlot slot) {
        ParkingSlotResponseDTO dto = new ParkingSlotResponseDTO();
        dto.setSlotId(slot.getSlotId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setActive(slot.isActive());
        return dto;
    }
}
