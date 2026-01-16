package com.ey.service.impl;

import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.User;
import com.ey.exception.ResourceNotFoundException;
import com.ey.exception.UnauthorizedException;
import com.ey.repository.ParkingLocationRepository;
import com.ey.repository.ParkingSlotRepository;
import com.ey.repository.UserRepository;
import com.ey.service.ParkingProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingProviderServiceImpl implements ParkingProviderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingLocationRepository locationRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;


    @Override
    public ParkingLocation addLocation(Long providerId, ParkingLocation location) {

        User provider = userRepository.findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parking provider not found with id " + providerId));

        location.setParkingProvider(provider);
        return locationRepository.save(location);
    }

    @Override
    public ParkingLocation updateLocation(Long providerId, Long locationId, ParkingLocation location) {

        ParkingLocation existingLocation = locationRepository.findById(locationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Location not found with id " + locationId));

        if (!existingLocation.getParkingProvider().getUserId().equals(providerId)) {
            throw new UnauthorizedException("Unauthorized to update this parking location");
        }

        existingLocation.setLocationName(location.getLocationName());
        existingLocation.setAddress(location.getAddress());
        existingLocation.setCity(location.getCity());
        existingLocation.setActive(location.isActive());

        return locationRepository.save(existingLocation);
    }

    @Override
    public List<ParkingLocation> getLocationsByProvider(Long providerId) {

        if (!userRepository.existsById(providerId)) {
            throw new ResourceNotFoundException("Parking provider not found with id " + providerId);
        }

        return locationRepository.findByParkingProviderUserId(providerId);
    }


    @Override
    public ParkingSlot addSlot(Long locationId, ParkingSlot slot) {

        ParkingLocation location = locationRepository.findById(locationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parking location not found with id " + locationId));

        slot.setLocation(location);
        slot.setActive(true);

        return slotRepository.save(slot);
    }

    @Override
    public List<ParkingSlot> getSlotsByLocation(Long locationId) {

        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Parking location not found with id " + locationId);
        }

        return slotRepository.findByLocationLocationId(locationId);
    }

    @Override
    public ParkingSlot activateSlot(Long slotId) {

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Slot not found with id " + slotId));

        slot.setActive(true);
        return slotRepository.save(slot);
    }

    @Override
    public ParkingSlot deactivateSlot(Long slotId) {

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Slot not found with id " + slotId));

        slot.setActive(false);
        return slotRepository.save(slot);
    }
}
