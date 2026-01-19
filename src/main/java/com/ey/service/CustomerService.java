package com.ey.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.Vehicle;

public interface CustomerService {

    CustomerIdProof addIdProof(Long customerId, CustomerIdProof idProof);
    CustomerIdProof getIdProof(Long customerId);

    Vehicle addVehicle(Long customerId, Vehicle vehicle);
    List<Vehicle> getVehiclesByCustomer(Long customerId);

    List<ParkingLocation> getParkingLocationsByCity(String city);

    List<ParkingSlot> getAvailableSlots(
            Long locationId,
            LocalDateTime startTime,
            LocalDateTime endTime);

}
