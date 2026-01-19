package com.ey;

import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.entity.User;
import com.ey.exception.ResourceNotFoundException;
import com.ey.exception.UnauthorizedException;
import com.ey.repository.ParkingLocationRepository;
import com.ey.repository.ParkingSlotRepository;
import com.ey.repository.UserRepository;
import com.ey.service.impl.ParkingProviderServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingProviderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParkingLocationRepository locationRepository;

    @Mock
    private ParkingSlotRepository slotRepository;

    @InjectMocks
    private ParkingProviderServiceImpl parkingProviderService;


    @Test
    void addLocation_success() {

        User provider = new User();
        provider.setUserId(1L);

        ParkingLocation location = new ParkingLocation();
        location.setLocationName("Main Street Parking");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(provider));
        when(locationRepository.save(any()))
                .thenReturn(location);

        ParkingLocation result =
                parkingProviderService.addLocation(1L, location);

        assertNotNull(result);
        assertEquals("Main Street Parking", result.getLocationName());
    }

    @Test
    void addLocation_providerNotFound() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                parkingProviderService.addLocation(99L, new ParkingLocation()));
    }


    @Test
    void updateLocation_success() {

        User provider = new User();
        provider.setUserId(1L);

        ParkingLocation existing = new ParkingLocation();
        existing.setLocationId(10L);
        existing.setParkingProvider(provider);

        ParkingLocation update = new ParkingLocation();
        update.setLocationName("Updated Location");
        update.setCity("Chennai");
        update.setAddress("MG Road");
        update.setActive(true);

        when(locationRepository.findById(10L))
                .thenReturn(Optional.of(existing));
        when(locationRepository.save(any()))
                .thenReturn(existing);

        ParkingLocation result =
                parkingProviderService.updateLocation(1L, 10L, update);

        assertEquals("Updated Location", result.getLocationName());
    }

    @Test
    void updateLocation_unauthorized() {

        User provider = new User();
        provider.setUserId(1L);

        User otherProvider = new User();
        otherProvider.setUserId(2L);

        ParkingLocation location = new ParkingLocation();
        location.setParkingProvider(otherProvider);

        when(locationRepository.findById(10L))
                .thenReturn(Optional.of(location));

        assertThrows(UnauthorizedException.class, () ->
                parkingProviderService.updateLocation(1L, 10L, new ParkingLocation()));
    }


    @Test
    void addSlot_success() {

        ParkingLocation location = new ParkingLocation();
        location.setLocationId(5L);

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotNumber("A1");

        when(locationRepository.findById(5L))
                .thenReturn(Optional.of(location));
        when(slotRepository.save(any()))
                .thenReturn(slot);

        ParkingSlot result =
                parkingProviderService.addSlot(5L, slot);

        assertTrue(result.isActive());
        assertEquals("A1", result.getSlotNumber());
    }

    @Test
    void activateSlot_success() {

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId(3L);
        slot.setActive(false);

        when(slotRepository.findById(3L))
                .thenReturn(Optional.of(slot));
        when(slotRepository.save(any()))
                .thenReturn(slot);

        ParkingSlot result =
                parkingProviderService.activateSlot(3L);

        assertTrue(result.isActive());
    }

    @Test
    void deactivateSlot_success() {

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId(4L);
        slot.setActive(true);

        when(slotRepository.findById(4L))
                .thenReturn(Optional.of(slot));
        when(slotRepository.save(any()))
                .thenReturn(slot);

        ParkingSlot result =
                parkingProviderService.deactivateSlot(4L);

        assertFalse(result.isActive());
    }
}
