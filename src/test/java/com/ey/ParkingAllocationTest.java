package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ey.entity.ParkingAllocation;

class ParkingAllocationTest {

    @Test
    void allocation_basic_test() {

        ParkingAllocation allocation = new ParkingAllocation();
        allocation.setAllocationId(1L);
        allocation.setStatus("BOOKED");

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        allocation.setStartTime(start);
        allocation.setEndTime(end);

        assertEquals("BOOKED", allocation.getStatus());
        assertTrue(allocation.getEndTime().isAfter(allocation.getStartTime()));
        assertNotEquals("CANCELLED", allocation.getStatus());
    }
}
