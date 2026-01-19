package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ey.entity.Vehicle;

class VehicleTest {

    @Test
    void vehicle_basic_test() {

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1L);
        vehicle.setVehicleNumber("TN01AB1234");
        vehicle.setVehicleType("CAR");

        assertNotNull(vehicle);
        assertEquals("TN01AB1234", vehicle.getVehicleNumber());
        assertEquals("CAR", vehicle.getVehicleType());
        assertNotEquals("BIKE", vehicle.getVehicleType());
    }
}
