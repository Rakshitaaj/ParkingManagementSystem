package com.ey.controller;

import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.service.ParkingProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ParkingProviderController {

    @Autowired
    private ParkingProviderService parkingProviderService;

    // ================= PARKING LOCATION =================

    // Add parking location
    @PostMapping("/{providerId}/locations")
    public ResponseEntity<ParkingLocation> addLocation(
            @PathVariable Long providerId,
            @RequestBody ParkingLocation location) {

        return ResponseEntity.ok(
                parkingProviderService.addLocation(providerId, location));
    }

    // Update parking location
    @PutMapping("/{providerId}/locations/{locationId}")
    public ResponseEntity<ParkingLocation> updateLocation(
            @PathVariable Long providerId,
            @PathVariable Long locationId,
            @RequestBody ParkingLocation location) {

        return ResponseEntity.ok(
                parkingProviderService.updateLocation(providerId, locationId, location));
    }

    // Get locations by provider
    @GetMapping("/{providerId}/locations")
    public ResponseEntity<List<ParkingLocation>> getLocationsByProvider(
            @PathVariable Long providerId) {

        return ResponseEntity.ok(
                parkingProviderService.getLocationsByProvider(providerId));
    }

    // ================= PARKING SLOT =================

    // Add parking slot
    @PostMapping("/locations/{locationId}/slots")
    public ResponseEntity<ParkingSlot> addSlot(
            @PathVariable Long locationId,
            @RequestBody ParkingSlot slot) {

        return ResponseEntity.ok(
                parkingProviderService.addSlot(locationId, slot));
    }

    // Get slots by location
    @GetMapping("/locations/{locationId}/slots")
    public ResponseEntity<List<ParkingSlot>> getSlotsByLocation(
            @PathVariable Long locationId) {

        return ResponseEntity.ok(
                parkingProviderService.getSlotsByLocation(locationId));
    }

    // Activate slot
    @PutMapping("/slots/{slotId}/activate")
    public ResponseEntity<ParkingSlot> activateSlot(
            @PathVariable Long slotId) {

        return ResponseEntity.ok(
                parkingProviderService.activateSlot(slotId));
    }

    // Deactivate slot
    @PutMapping("/slots/{slotId}/deactivate")
    public ResponseEntity<ParkingSlot> deactivateSlot(
            @PathVariable Long slotId) {

        return ResponseEntity.ok(
                parkingProviderService.deactivateSlot(slotId));
    }
}
