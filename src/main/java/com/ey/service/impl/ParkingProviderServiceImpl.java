package com.ey.service.impl; 

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.User;
import com.ey.exception.ResourceNotFoundException;
import com.ey.exception.UnauthorizedException;
import com.ey.repository.ParkingLocationRepository;
import com.ey.repository.ParkingSlotRepository;
import com.ey.repository.UserRepository;
import com.ey.service.ParkingProviderService;

@Service
public class ParkingProviderServiceImpl implements ParkingProviderService {

    private static final Logger logger =LoggerFactory.getLogger(ParkingProviderServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingLocationRepository locationRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;


    @Override
    public ParkingLocation addLocation(Long providerId, ParkingLocation location) {

        logger.info("Add parking location request for providerId {}", providerId);
        User provider = userRepository.findById(providerId).orElseThrow(() -> {
            logger.warn("Parking provider not found with id {}", providerId);
            return new ResourceNotFoundException("Parking provider not found with id " + providerId);
        });

        location.setParkingProvider(provider);
        ParkingLocation saved = locationRepository.save(location);
        logger.info("Parking location added successfully with locationId {}",saved.getLocationId());
        return saved;
    }

    @Override
    public ParkingLocation updateLocation(Long providerId, Long locationId, ParkingLocation location) {

        logger.info("Update parking location {} by providerId {}",locationId, providerId);

        ParkingLocation existingLocation =locationRepository.findById(locationId).orElseThrow(() -> {
                            logger.warn("Location not found with id {}", locationId);
                            return new ResourceNotFoundException("Location not found with id " + locationId);
                        });

        if (!existingLocation.getParkingProvider().getUserId().equals(providerId)) {
            logger.warn("Unauthorized update attempt by providerId {} for locationId {}",providerId, locationId);
            throw new UnauthorizedException("Unauthorized to update this parking location");
        }

        existingLocation.setLocationName(location.getLocationName());
        existingLocation.setAddress(location.getAddress());
        existingLocation.setCity(location.getCity());
        existingLocation.setActive(location.isActive());

        ParkingLocation updated = locationRepository.save(existingLocation);

        logger.info("Parking location {} updated successfully", locationId);
        return updated;
    }
    
    
    @Override
    public List<ParkingLocation> getLocationsByProvider(Long providerId) {

        logger.info("Fetch parking locations for providerId {}", providerId);

        if (!userRepository.existsById(providerId)) {
            logger.warn("Parking provider not found with id {}", providerId);
            throw new ResourceNotFoundException("Parking provider not found with id " + providerId);
        }
        return locationRepository.findByParkingProviderUserId(providerId);
    }


    @Override
    public ParkingSlot addSlot(Long locationId, ParkingSlot slot) {
        logger.info("Add slot request for locationId {}", locationId);

        ParkingLocation location =locationRepository.findById(locationId).orElseThrow(() -> {
                            logger.warn("Parking location not found with id {}", locationId);
                            return new ResourceNotFoundException("Parking location not found with id " + locationId);
                        });

        slot.setLocation(location);
        slot.setActive(true);

        ParkingSlot saved = slotRepository.save(slot);

        logger.info("Slot added successfully with slotId {}", saved.getSlotId());
        return saved;
    }

    @Override
    public List<ParkingSlot> getSlotsByLocation(Long locationId) {

        logger.info("Fetch slots for locationId {}", locationId);

        if (!locationRepository.existsById(locationId)) {
            logger.warn("Parking location not found with id {}", locationId);
            throw new ResourceNotFoundException("Parking location not found with id " + locationId);
        }

        return slotRepository.findByLocationLocationId(locationId);
    }

    
    
    @Override
    public ParkingSlot activateSlot(Long slotId) {

        logger.info("Activate slot request for slotId {}", slotId);

        ParkingSlot slot =slotRepository.findById(slotId).orElseThrow(() -> {
                            logger.warn("Slot not found with id {}", slotId);
                            return new ResourceNotFoundException("Slot not found with id " + slotId);
                        });

        slot.setActive(true);
        ParkingSlot saved = slotRepository.save(slot);

        logger.info("Slot {} activated successfully", slotId);
        return saved;
    }

    
    
    
    @Override
    public ParkingSlot deactivateSlot(Long slotId) {

        logger.info("Deactivate slot request for slotId {}", slotId);

        ParkingSlot slot =
                slotRepository.findById(slotId)
                        .orElseThrow(() -> {
                            logger.warn("Slot not found with id {}", slotId);
                            return new ResourceNotFoundException(
                                    "Slot not found with id " + slotId);
                        });

        slot.setActive(false);
        ParkingSlot saved = slotRepository.save(slot);

        logger.info("Slot {} deactivated successfully", slotId);
        return saved;
    }
    
    
    @Override
    public ParkingSlot updateSlot(Long slotId, ParkingSlot slot) {

        logger.info("Update slot request for slotId {}", slotId);

        ParkingSlot existing =
                slotRepository.findById(slotId)
                        .orElseThrow(() -> {
                            logger.warn("Slot not found with id {}", slotId);
                            return new ResourceNotFoundException("Slot not found");
                        });

        if (slot.getSlotNumber() != null) {
            existing.setSlotNumber(slot.getSlotNumber());
        }

        existing.setActive(slot.isActive());
        ParkingSlot updated = slotRepository.save(existing);

        logger.info("Slot {} updated successfully", slotId);
        return updated;
    }
    
    
    @Override
    public void deleteSlot(Long slotId) {

        logger.info("Delete slot request for slotId {}", slotId);

        ParkingSlot slot =
                slotRepository.findById(slotId)
                        .orElseThrow(() -> {
                            logger.warn("Slot not found with id {}", slotId);
                            return new ResourceNotFoundException("Slot not found");
                        });

        slotRepository.delete(slot);

        logger.info("Slot {} deleted successfully", slotId);
    }
}
