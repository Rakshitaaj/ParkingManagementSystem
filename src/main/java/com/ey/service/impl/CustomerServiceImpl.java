package com.ey.service.impl;

import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.User;
import com.ey.entity.Vehicle;
import com.ey.repository.CustomerIdProofRepository;
import com.ey.repository.ParkingLocationRepository;
import com.ey.repository.UserRepository;
import com.ey.repository.VehicleRepository;
import com.ey.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerIdProofRepository idProofRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingLocationRepository locationRepository;

    //ID PROOF

    @Override
    public CustomerIdProof addIdProof(Long customerId, CustomerIdProof idProof) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (idProofRepository.existsByIdProofNumber(idProof.getIdProofNumber())) {
            throw new RuntimeException("ID Proof already exists");
        }

        idProof.setCustomer(customer);
        return idProofRepository.save(idProof);
    }

    @Override
    public CustomerIdProof getIdProof(Long customerId) {
        return idProofRepository.findByCustomerUserId(customerId)
                .orElseThrow(() -> new RuntimeException("ID Proof not found"));
    }

    //VEHICLE 

    @Override
    public Vehicle addVehicle(Long customerId, Vehicle vehicle) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (vehicleRepository.existsByVehicleNumber(vehicle.getVehicleNumber())) {
            throw new RuntimeException("Vehicle already registered");
        }

        vehicle.setCustomer(customer);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getVehiclesByCustomer(Long customerId) {
        return vehicleRepository.findByCustomerUserId(customerId);
    }

    //PARKING SEARCH

    @Override
    public List<ParkingLocation> getParkingLocationsByCity(String city) {
        return locationRepository.findByCity(city);
    }
}
