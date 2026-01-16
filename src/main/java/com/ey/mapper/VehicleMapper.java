package com.ey.mapper;

import com.ey.dto.request.VehicleRequestDTO;
import com.ey.dto.response.VehicleResponseDTO;
import com.ey.entity.Vehicle;

public class VehicleMapper {

    public static Vehicle toEntity(VehicleRequestDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setVehicleType(dto.getVehicleType());
        return vehicle;
    }

    public static VehicleResponseDTO toResponse(Vehicle vehicle) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setVehicleNumber(vehicle.getVehicleNumber());
        dto.setVehicleType(vehicle.getVehicleType());
        return dto;
    }
}
