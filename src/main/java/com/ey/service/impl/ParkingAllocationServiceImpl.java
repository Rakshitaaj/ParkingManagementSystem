package com.ey.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.entity.ParkingAllocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.User;
import com.ey.entity.Vehicle;
import com.ey.exception.BadRequestException;
import com.ey.exception.ConflictException;
import com.ey.exception.ResourceNotFoundException;
import com.ey.repository.ParkingAllocationRepository;
import com.ey.repository.ParkingSlotRepository;
import com.ey.repository.UserRepository;
import com.ey.repository.VehicleRepository;
import com.ey.service.ParkingAllocationService;

@Service
public class ParkingAllocationServiceImpl implements ParkingAllocationService {

    @Autowired
    private ParkingAllocationRepository allocationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Override
    public ParkingAllocation allocateSlot(
            Long customerId,
            Long vehicleId,
            Long slotId,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Start time must be before end time");
        }

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        
        
        if (vehicle.getCustomer() == null ||
                !vehicle.getCustomer().getUserId().equals(customerId)) {
                throw new BadRequestException(
                        "Vehicle does not belong to this customer");
            }

            ParkingSlot slot = slotRepository.findById(slotId)
                    .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

            if (Boolean.FALSE.equals(slot.isActive())) {
                throw new BadRequestException("Slot is not active");
            }


        boolean slotAlreadyBooked =
                allocationRepository.existsBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        slotId, endTime, startTime
                );

        if (slotAlreadyBooked) {
            throw new ConflictException("Slot already booked for given time");
        }

        ParkingAllocation allocation = new ParkingAllocation();
        allocation.setCustomer(customer);
        allocation.setVehicle(vehicle);
        allocation.setSlot(slot);
        allocation.setStartTime(startTime);
        allocation.setEndTime(endTime);
        allocation.setStatus("BOOKED");

        return allocationRepository.save(allocation);
    }

    @Override
    public List<ParkingAllocation> getAllocationsByCustomer(Long customerId) {
        return allocationRepository.findByCustomerUserId(customerId);
    }

    @Override
    public List<ParkingAllocation> getAllocationsBySlot(Long slotId) {
        return allocationRepository.findBySlotSlotId(slotId);
    }

    @Override
    public ParkingAllocation cancelAllocation(Long allocationId) {

        ParkingAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        allocation.setStatus("CANCELLED");
        return allocationRepository.save(allocation);
    }
    
    @Override
    public ParkingAllocation getAllocationById(Long allocationId) {
        return allocationRepository.findById(allocationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Allocation not found"));
    }
    
    @Override
    public ParkingAllocation extendAllocationTime(
            Long allocationId,
            LocalDateTime newEndTime) {

        ParkingAllocation allocation =
                allocationRepository.findById(allocationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Allocation not found"));

        if (newEndTime.isBefore(allocation.getEndTime())) {
            throw new BadRequestException(
                    "New end time must be after current end time");
        }

        boolean conflict =
                allocationRepository
                        .existsBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
                                allocation.getSlot().getSlotId(),
                                newEndTime,
                                allocation.getEndTime());

        if (conflict) {
            throw new ConflictException(
                    "Slot already booked for the extended time");
        }

        allocation.setEndTime(newEndTime);
        return allocationRepository.save(allocation);
    }
    
  
    




}
