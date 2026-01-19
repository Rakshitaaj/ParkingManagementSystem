package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ey.entity.ParkingLocation;
import com.ey.entity.User;

class ParkingLocationTest {

    @Test
    void parking_location_basic_test() {

        User provider = new User();
        provider.setUserId(5L);

        ParkingLocation location = new ParkingLocation();
        location.setLocationId(1L);
        location.setLocationName("City Center Parking");
        location.setCity("Chennai");
        location.setAddress("MG Road");
        location.setActive(true);
        location.setParkingProvider(provider);

        assertNotNull(location);
        assertEquals("City Center Parking", location.getLocationName());
        assertEquals("Chennai", location.getCity());
        assertTrue(location.isActive());
        assertNotNull(location.getParkingProvider());
        assertEquals(5L, location.getParkingProvider().getUserId());
    }
}
