package com.ey.controller;

import com.ey.dto.request.ParkingAllocationRequestDTO;
import com.ey.dto.response.ParkingAllocationResponseDTO;
import com.ey.entity.ParkingAllocation;
import com.ey.mapper.ParkingAllocationMapper;
import com.ey.service.ParkingAllocationService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class ParkingAllocationController {

    @Autowired
    private ParkingAllocationService parkingAllocationService;

    // ================= ALLOCATE SLOT =================

    @PostMapping
    public ResponseEntity<ParkingAllocationResponseDTO> allocateSlot(
            @Valid @RequestBody ParkingAllocationRequestDTO request) {

        ParkingAllocation allocation =
                parkingAllocationService.allocateSlot(
                        request.getCustomerId(),
                        request.getVehicleId(),
                        request.getSlotId(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        return ResponseEntity.ok(
                ParkingAllocationMapper.toResponse(allocation));
    }

    // ================= GET =================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ParkingAllocationResponseDTO>> getAllocationsByCustomer(
            @PathVariable Long customerId) {

        List<ParkingAllocation> allocations =
                parkingAllocationService.getAllocationsByCustomer(customerId);

        List<ParkingAllocationResponseDTO> response =
                allocations.stream()
                        .map(ParkingAllocationMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/slot/{slotId}")
    public ResponseEntity<List<ParkingAllocationResponseDTO>> getAllocationsBySlot(
            @PathVariable Long slotId) {

        List<ParkingAllocation> allocations =
                parkingAllocationService.getAllocationsBySlot(slotId);

        List<ParkingAllocationResponseDTO> response =
                allocations.stream()
                        .map(ParkingAllocationMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // ================= CANCEL =================

    @PutMapping("/{allocationId}/cancel")
    public ResponseEntity<ParkingAllocationResponseDTO> cancelAllocation(
            @PathVariable Long allocationId) {

        ParkingAllocation allocation =
                parkingAllocationService.cancelAllocation(allocationId);

        return ResponseEntity.ok(
                ParkingAllocationMapper.toResponse(allocation));
    }
}
