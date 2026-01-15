package com.ey.service;

import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.Vehicle;

import java.util.List;

public interface CustomerService {

    // ID Proof
    CustomerIdProof addIdProof(Long customerId, CustomerIdProof idProof);
    CustomerIdProof getIdProof(Long customerId);

    // Vehicle
    Vehicle addVehicle(Long customerId, Vehicle vehicle);
    List<Vehicle> getVehiclesByCustomer(Long customerId);

    // Parking search
    List<ParkingLocation> getParkingLocationsByCity(String city);
}
