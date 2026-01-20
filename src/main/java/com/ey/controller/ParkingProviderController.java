package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.ParkingLocationRequestDTO;
import com.ey.dto.request.ParkingSlotRequestDTO;
import com.ey.dto.response.ParkingLocationResponseDTO;
import com.ey.dto.response.ParkingSlotResponseDTO;
import com.ey.entity.ParkingLocation;
import com.ey.entity.ParkingSlot;
import com.ey.mapper.ParkingLocationMapper;
import com.ey.mapper.ParkingSlotMapper;
import com.ey.service.ParkingProviderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/providers")
public class ParkingProviderController {

    @Autowired
    private ParkingProviderService parkingProviderService;

    @PostMapping("/{providerId}/locations")
    public ResponseEntity<ParkingLocationResponseDTO> addLocation(@PathVariable Long providerId,@Valid @RequestBody ParkingLocationRequestDTO request) {
        ParkingLocation location =ParkingLocationMapper.toEntity(request);
        ParkingLocation saved =parkingProviderService.addLocation(providerId, location);
        return ResponseEntity.ok(ParkingLocationMapper.toResponse(saved));
    }

    
    
    @PutMapping("/{providerId}/locations/{locationId}")
    public ResponseEntity<ParkingLocationResponseDTO> updateLocation(@PathVariable Long providerId,@PathVariable Long locationId,@Valid @RequestBody ParkingLocationRequestDTO request){

        ParkingLocation location =ParkingLocationMapper.toEntity(request);
        ParkingLocation updated =parkingProviderService.updateLocation(providerId, locationId, location);
        return ResponseEntity.ok(ParkingLocationMapper.toResponse(updated));
    }
    
    

    @GetMapping("/{providerId}/locations")
    public ResponseEntity<List<ParkingLocationResponseDTO>> getLocationsByProvider(@PathVariable Long providerId) {
        List<ParkingLocation> locations =parkingProviderService.getLocationsByProvider(providerId);
        List<ParkingLocationResponseDTO> response =locations.stream().map(ParkingLocationMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }
    
    
    
    
    @PostMapping("/locations/{locationId}/slots")
    public ResponseEntity<ParkingSlotResponseDTO> addSlot(@PathVariable Long locationId,@Valid @RequestBody ParkingSlotRequestDTO request) {
        ParkingSlot slot =ParkingSlotMapper.toEntity(request);
        ParkingSlot saved =parkingProviderService.addSlot(locationId, slot);
        return ResponseEntity.ok(ParkingSlotMapper.toResponse(saved));
    }
    
    
    

    @GetMapping("/locations/{locationId}/slots")
    public ResponseEntity<List<ParkingSlotResponseDTO>> getSlotsByLocation(@PathVariable Long locationId) {
        List<ParkingSlot> slots =parkingProviderService.getSlotsByLocation(locationId);
        List<ParkingSlotResponseDTO> response =slots.stream().map(ParkingSlotMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/slots/{slotId}/activate")
    public ResponseEntity<ParkingSlotResponseDTO> activateSlot(@PathVariable Long slotId) {
        ParkingSlot slot =parkingProviderService.activateSlot(slotId);
        return ResponseEntity.ok(ParkingSlotMapper.toResponse(slot));
    }

    @PutMapping("/slots/{slotId}/deactivate")
    public ResponseEntity<ParkingSlotResponseDTO> deactivateSlot(@PathVariable Long slotId) {
        ParkingSlot slot =parkingProviderService.deactivateSlot(slotId);
        return ResponseEntity.ok(ParkingSlotMapper.toResponse(slot));
    }
    
    @PutMapping("/slots/{slotId}")
    public ResponseEntity<ParkingSlotResponseDTO> updateSlot(@PathVariable Long slotId,@RequestBody ParkingSlotRequestDTO request) {
        ParkingSlot slot = new ParkingSlot();
        slot.setSlotNumber(request.getSlotNumber());
        slot.setActive(request.getActive()); 
        return ResponseEntity.ok(ParkingSlotMapper.toResponse(parkingProviderService.updateSlot(slotId, slot)));
    }
    
    



}
