package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;

class ParkingSlotTest {

    @Test
    void parking_slot_basic_test() {

        ParkingLocation location = new ParkingLocation();
        location.setLocationId(3L);

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId(1L);
        slot.setSlotNumber("A1");
        slot.setActive(true);
        slot.setLocation(location);

        assertNotNull(slot);
        assertEquals("A1", slot.getSlotNumber());
        assertTrue(slot.isActive());
        assertNotNull(slot.getLocation());
        assertEquals(3L, slot.getLocation().getLocationId());
    }
}
