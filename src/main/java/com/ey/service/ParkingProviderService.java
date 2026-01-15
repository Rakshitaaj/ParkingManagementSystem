package com.ey.service;

import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;

import java.util.List;

public interface ParkingProviderService {

    // Parking Location
    ParkingLocation addLocation(Long providerId, ParkingLocation location);
    ParkingLocation updateLocation(Long providerId, Long locationId, ParkingLocation location);
    List<ParkingLocation> getLocationsByProvider(Long providerId);

    // Parking Slot
    ParkingSlot addSlot(Long locationId, ParkingSlot slot);
    List<ParkingSlot> getSlotsByLocation(Long locationId);
    ParkingSlot activateSlot(Long slotId);
    ParkingSlot deactivateSlot(Long slotId);
}
