package com.ey.service.impl;

import com.ey.entity.ParkingAllocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.User;
import com.ey.entity.Vehicle;
import com.ey.repository.ParkingAllocationRepository;
import com.ey.repository.ParkingSlotRepository;
import com.ey.repository.UserRepository;
import com.ey.repository.VehicleRepository;
import com.ey.service.ParkingAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
            throw new RuntimeException("Start time must be before end time");
        }

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isActive()) {
            throw new RuntimeException("Slot is not active");
        }

        // 🔥 CORE LOGIC: Overlap check
        boolean slotAlreadyBooked =
                allocationRepository.existsBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        slotId, endTime, startTime
                );

        if (slotAlreadyBooked) {
            throw new RuntimeException("Slot already booked for given time");
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
                .orElseThrow(() -> new RuntimeException("Allocation not found"));

        allocation.setStatus("CANCELLED");
        return allocationRepository.save(allocation);
    }
}
