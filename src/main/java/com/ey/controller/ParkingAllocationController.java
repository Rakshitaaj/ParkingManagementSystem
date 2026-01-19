package com.ey.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.ParkingAllocationResponseDTO;
import com.ey.entity.ParkingAllocation;
import com.ey.mapper.ParkingAllocationMapper;
import com.ey.service.ParkingAllocationService;

@RestController
@RequestMapping("/api/allocations")
public class ParkingAllocationController {

    @Autowired
    private ParkingAllocationService parkingAllocationService;


    @PostMapping
    public ResponseEntity<ParkingAllocation> allocateSlot(
            @RequestParam Long customerId,
            @RequestParam Long vehicleId,
            @RequestParam Long slotId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime) {

        return ResponseEntity.ok(
                parkingAllocationService.allocateSlot(
                        customerId, vehicleId, slotId, startTime, endTime));
    }


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


    @PutMapping("/{allocationId}/cancel")
    public ResponseEntity<ParkingAllocationResponseDTO> cancelAllocation(
            @PathVariable Long allocationId) {

        ParkingAllocation allocation =
                parkingAllocationService.cancelAllocation(allocationId);

        return ResponseEntity.ok(
                ParkingAllocationMapper.toResponse(allocation));
    }
    
    @GetMapping("/{allocationId}")
    public ResponseEntity<ParkingAllocationResponseDTO> getAllocationById(
            @PathVariable Long allocationId) {

        ParkingAllocation allocation =
                parkingAllocationService.getAllocationById(allocationId);

        return ResponseEntity.ok(
                ParkingAllocationMapper.toResponse(allocation));
    }
    
    @PutMapping("/{allocationId}/extend")
    public ResponseEntity<ParkingAllocationResponseDTO> extendAllocation(
            @PathVariable Long allocationId,
            @RequestParam LocalDateTime newEndTime) {

        return ResponseEntity.ok(
                ParkingAllocationMapper.toResponse(
                        parkingAllocationService
                                .extendAllocationTime(allocationId, newEndTime)));
    }


}
