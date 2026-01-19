package com.ey.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger =LoggerFactory.getLogger(ParkingAllocationServiceImpl.class);

    @Autowired
    private ParkingAllocationRepository allocationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Override
    public ParkingAllocation allocateSlot(Long customerId,Long vehicleId,Long slotId,LocalDateTime startTime,LocalDateTime endTime) {

        logger.info("Allocate slot request: customerId={}, vehicleId={}, slotId={}, startTime={}, endTime={}",customerId, vehicleId, slotId, startTime, endTime);

        if (startTime.isAfter(endTime)) {
            logger.error("Invalid time range: startTime {} is after endTime {}",startTime, endTime);
            throw new BadRequestException("Start time must be before end time");
        }

        User customer = userRepository.findById(customerId).orElseThrow(() -> {
                    logger.warn("Customer not found: {}", customerId);
                    return new ResourceNotFoundException("Customer not found");
                });

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> {
                    logger.warn("Vehicle not found: {}", vehicleId);
                    return new ResourceNotFoundException("Vehicle not found");
                });

        if (vehicle.getCustomer() == null ||!vehicle.getCustomer().getUserId().equals(customerId)) {

            logger.warn("Vehicle {} does not belong to customer {}",vehicleId, customerId);
            throw new BadRequestException("Vehicle does not belong to this customer");
        }

        ParkingSlot slot = slotRepository.findById(slotId).orElseThrow(() -> {
                    logger.warn("Slot not found: {}", slotId);
                    return new ResourceNotFoundException("Slot not found");
                });

        if (Boolean.FALSE.equals(slot.isActive())) {
            logger.warn("Inactive slot allocation attempted: {}", slotId);
            throw new BadRequestException("Slot is not active");
        }

        boolean slotAlreadyBooked =allocationRepository.existsBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(slotId, endTime, startTime);

        if (slotAlreadyBooked) {
            logger.warn("Slot {} already booked between {} and {}",slotId, startTime, endTime);
            throw new ConflictException("Slot already booked for given time");
        }

        ParkingAllocation allocation = new ParkingAllocation();
        allocation.setCustomer(customer);
        allocation.setVehicle(vehicle);
        allocation.setSlot(slot);
        allocation.setStartTime(startTime);
        allocation.setEndTime(endTime);
        allocation.setStatus("BOOKED");

        ParkingAllocation saved = allocationRepository.save(allocation);

        logger.info("Slot allocated successfully: allocationId={}, slotId={}",saved.getAllocationId(), slotId);

        return saved;
    }

    @Override
    public List<ParkingAllocation> getAllocationsByCustomer(Long customerId) {

        logger.info("Fetch allocations for customerId {}", customerId);
        return allocationRepository.findByCustomerUserId(customerId);
    }

    @Override
    public List<ParkingAllocation> getAllocationsBySlot(Long slotId) {

        logger.info("Fetch allocations for slotId {}", slotId);
        return allocationRepository.findBySlotSlotId(slotId);
    }

  
    @Override
    public ParkingAllocation cancelAllocation(Long allocationId) {

        logger.info("Cancel allocation request for allocationId {}", allocationId);

        ParkingAllocation allocation =allocationRepository.findById(allocationId).orElseThrow(() -> {
                            logger.warn("Allocation not found for cancellation: {}",allocationId);
                            return new ResourceNotFoundException("Allocation not found");
                        });

        allocation.setStatus("CANCELLED");
        ParkingAllocation saved =allocationRepository.save(allocation);

        logger.info("Allocation {} cancelled successfully", allocationId);
        return saved;
    }

    @Override
    public ParkingAllocation getAllocationById(Long allocationId) {

        logger.info("Fetch allocation by id {}", allocationId);

        return allocationRepository.findById(allocationId).orElseThrow(() -> {
                    logger.warn("Allocation not found: {}", allocationId);
                    return new ResourceNotFoundException("Allocation not found");
                });
    }

  
    @Override
    public ParkingAllocation extendAllocationTime(Long allocationId,LocalDateTime newEndTime) {

        logger.info("Extend allocation time request: allocationId={}, newEndTime={}",allocationId, newEndTime);

        ParkingAllocation allocation =allocationRepository.findById(allocationId).orElseThrow(() -> {
                            logger.warn("Allocation not found for extension: {}",allocationId);
                            return new ResourceNotFoundException("Allocation not found");
                        });

        if (newEndTime.isBefore(allocation.getEndTime())) {
            logger.error("Invalid extension time: {} before existing {}",newEndTime, allocation.getEndTime());
            throw new BadRequestException("New end time must be after current end time");
        }

        boolean conflict =allocationRepository.existsBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(allocation.getSlot().getSlotId(),
                                newEndTime,allocation.getEndTime());

        if (conflict) {
            logger.warn("Extension conflict for allocationId {} on slot {}",allocationId,allocation.getSlot().getSlotId());
            throw new ConflictException("Slot already booked for the extended time");
        }

        allocation.setEndTime(newEndTime);
        ParkingAllocation saved =allocationRepository.save(allocation);
        logger.info("Allocation {} extended successfully till {}",allocationId, newEndTime);
        return saved;
    }
}
