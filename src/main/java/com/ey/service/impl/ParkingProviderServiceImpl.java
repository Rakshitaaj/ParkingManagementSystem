package com.ey.service.impl;

import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.User;
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

    //PARKING LOCATION 

    @Override
    public ParkingLocation addLocation(Long providerId, ParkingLocation location) {

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Parking provider not found"));

        location.setParkingProvider(provider);
        return locationRepository.save(location);
    }

    @Override
    public ParkingLocation updateLocation(Long providerId, Long locationId, ParkingLocation location) {

        ParkingLocation existingLocation = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        if (!existingLocation.getParkingProvider().getUserId().equals(providerId)) {
            throw new RuntimeException("Unauthorized to update this location");
        }

        existingLocation.setLocationName(location.getLocationName());
        existingLocation.setAddress(location.getAddress());
        existingLocation.setCity(location.getCity());
        existingLocation.setActive(location.isActive());

        return locationRepository.save(existingLocation);
    }

    @Override
    public List<ParkingLocation> getLocationsByProvider(Long providerId) {
        return locationRepository.findByParkingProviderUserId(providerId);
    }

    //PARKING SLOT

    @Override
    public ParkingSlot addSlot(Long locationId, ParkingSlot slot) {

        ParkingLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Parking location not found"));

        slot.setLocation(location);
        slot.setActive(true);

        return slotRepository.save(slot);
    }

    @Override
    public List<ParkingSlot> getSlotsByLocation(Long locationId) {
        return slotRepository.findByLocationLocationId(locationId);
    }

    @Override
    public ParkingSlot activateSlot(Long slotId) {

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        slot.setActive(true);
        return slotRepository.save(slot);
    }

    @Override
    public ParkingSlot deactivateSlot(Long slotId) {

        ParkingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        slot.setActive(false);
        return slotRepository.save(slot);
    }
}
