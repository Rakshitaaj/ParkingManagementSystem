package com.ey.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.User;
import com.ey.entity.Vehicle;
import com.ey.exception.BadRequestException;
import com.ey.exception.ConflictException;
import com.ey.exception.ResourceNotFoundException;
import com.ey.repository.CustomerIdProofRepository;
import com.ey.repository.ParkingAllocationRepository;
import com.ey.repository.ParkingLocationRepository;
import com.ey.repository.ParkingSlotRepository;
import com.ey.repository.UserRepository;
import com.ey.repository.VehicleRepository;
import com.ey.service.CustomerService;

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
    
    @Autowired
    private ParkingSlotRepository slotRepository;
    
    @Autowired
    private ParkingAllocationRepository allocationRepository;


    @Override
    public CustomerIdProof addIdProof(Long customerId, CustomerIdProof idProof) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id " + customerId));

        if (idProofRepository.existsByIdProofNumber(idProof.getIdProofNumber())) {
            throw new ConflictException("ID Proof already exists");
        }

        idProof.setCustomer(customer);
        return idProofRepository.save(idProof);
    }

    @Override
    public CustomerIdProof getIdProof(Long customerId) {

        return idProofRepository.findByCustomerUserId(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ID Proof not found for customer " + customerId));
    }


    @Override
    public Vehicle addVehicle(Long customerId, Vehicle vehicle) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id " + customerId));

        if (vehicleRepository.existsByVehicleNumber(vehicle.getVehicleNumber())) {
            throw new ConflictException("Vehicle already registered");
        }

        vehicle.setCustomer(customer);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getVehiclesByCustomer(Long customerId) {

        if (!userRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id " + customerId);
        }

        return vehicleRepository.findByCustomerUserId(customerId);
    }


    @Override
    public List<ParkingLocation> getParkingLocationsByCity(String city) {
        return locationRepository.findByCity(city);
    }
    @Override
    public List<ParkingSlot> getAvailableSlots(
            Long locationId,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Start time must be before end time");
        }

        List<ParkingSlot> activeSlots =
                slotRepository
                        .findByLocationLocationIdAndActiveTrue(locationId);

        return activeSlots.stream()
                .filter(slot ->
                        allocationRepository
                                .findBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
                                        slot.getSlotId(),
                                        endTime,
                                        startTime)
                                .isEmpty()).toList();
    }

}
