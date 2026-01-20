package com.ey.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

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

		logger.info("Add ID proof request for customerId {}", customerId);
		User customer = userRepository.findById(customerId).orElseThrow(() -> {
			logger.warn("Customer not found while adding ID proof: {}", customerId);
			return new ResourceNotFoundException("Customer not found with id " + customerId);
		});

		if (idProofRepository.existsByIdProofNumber(idProof.getIdProofNumber())) {
			logger.warn("Duplicate ID proof number attempted: {}", idProof.getIdProofNumber());
			throw new ConflictException("ID Proof already exists");
		}
		idProof.setCustomer(customer);
		CustomerIdProof saved = idProofRepository.save(idProof);

		logger.info("ID proof added successfully for customerId {}", customerId);
		return saved;
	}

	@Override
	public CustomerIdProof getIdProof(Long customerId) {

		logger.info("Fetch ID proof for customerId {}", customerId);

		return idProofRepository.findByCustomerUserId(customerId).orElseThrow(() -> {
			logger.warn("ID proof not found for customerId {}", customerId);
			return new ResourceNotFoundException("ID Proof not found for customer " + customerId);
		});
	}

	
	
	@Override
	public Vehicle addVehicle(Long customerId, Vehicle vehicle) {

		logger.info("Add vehicle request for customerId {}", customerId);
		User customer = userRepository.findById(customerId).orElseThrow(() -> {
			logger.warn("Customer not found while adding vehicle: {}", customerId);
			return new ResourceNotFoundException("Customer not found with id " + customerId);
		});
		if (vehicleRepository.existsByVehicleNumber(vehicle.getVehicleNumber())) {
			logger.warn("Duplicate vehicle registration attempted: {}", vehicle.getVehicleNumber());
			throw new ConflictException("Vehicle already registered");
		}
		vehicle.setCustomer(customer);
		Vehicle saved = vehicleRepository.save(vehicle);
		logger.info("Vehicle {} added successfully for customerId {}", saved.getVehicleNumber(), customerId);

		return saved;
	}

	@Override
	public List<Vehicle> getVehiclesByCustomer(Long customerId) {
		logger.info("Fetch vehicles for customerId {}", customerId);

		if (!userRepository.existsById(customerId)) {
			logger.warn("Customer not found while fetching vehicles: {}", customerId);
			throw new ResourceNotFoundException("Customer not found with id " + customerId);
		}
		return vehicleRepository.findByCustomerUserId(customerId);
	}
	
	
	
	@Override
	public List<ParkingLocation> getParkingLocationsByCity(String city) {

		logger.info("Search parking locations for city {}", city);
		return locationRepository.findByCity(city);
	}
	

	@Override
	public List<ParkingSlot> getAvailableSlots(Long locationId, LocalDateTime startTime, LocalDateTime endTime) {

		logger.info("Check available slots for locationId {} between {} and {}", locationId, startTime, endTime);
		if (startTime.isAfter(endTime)) {
			logger.error("Invalid time range: startTime {} is after endTime {}", startTime, endTime);
			throw new BadRequestException("Start time must be before end time");
		}
		
		
		List<ParkingSlot> activeSlots = slotRepository.findByLocationLocationIdAndActiveTrue(locationId);
		List<ParkingSlot> availableSlots = activeSlots.stream().filter(slot -> allocationRepository.findBySlotSlotIdAndStartTimeLessThanAndEndTimeGreaterThan(slot.getSlotId(), endTime, startTime)
						.isEmpty()).toList();

		logger.info("Available slots found: {} for locationId {}", availableSlots.size(), locationId);
		return availableSlots;
	}
}
