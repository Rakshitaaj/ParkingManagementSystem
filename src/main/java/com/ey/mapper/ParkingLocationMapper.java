package com.ey.mapper;

import com.ey.dto.request.ParkingLocationRequestDTO;
import com.ey.dto.response.ParkingLocationResponseDTO;
import com.ey.entity.ParkingLocation;

public class ParkingLocationMapper {

    public static ParkingLocation toEntity(ParkingLocationRequestDTO dto) {
        ParkingLocation location = new ParkingLocation();
        location.setLocationName(dto.getLocationName());
        location.setAddress(dto.getAddress());
        location.setCity(dto.getCity());
        location.setActive(dto.isActive());
        return location;
    }

    public static ParkingLocationResponseDTO toResponse(ParkingLocation location) {
        ParkingLocationResponseDTO dto = new ParkingLocationResponseDTO();
        dto.setLocationId(location.getLocationId());
        dto.setLocationName(location.getLocationName());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());
        dto.setActive(location.isActive());
        return dto;
    }
}
