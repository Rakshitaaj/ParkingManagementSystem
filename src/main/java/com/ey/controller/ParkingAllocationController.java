package com.ey.controller;

import com.ey.entity.ParkingAllocation;
import com.ey.service.ParkingAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class ParkingAllocationController {

    @Autowired
    private ParkingAllocationService parkingAllocationService;

    //ALLOCATE SLOT

    @PostMapping
    public ResponseEntity<ParkingAllocation> allocateSlot(
            @RequestParam Long customerId,
            @RequestParam Long vehicleId,
            @RequestParam Long slotId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {

        return ResponseEntity.ok(
                parkingAllocationService.allocateSlot(
                        customerId, vehicleId, slotId, startTime, endTime));
    }


    // Get allocations by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ParkingAllocation>> getAllocationsByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                parkingAllocationService.getAllocationsByCustomer(customerId));
    }

    // Get allocations by slot
    @GetMapping("/slot/{slotId}")
    public ResponseEntity<List<ParkingAllocation>> getAllocationsBySlot(
            @PathVariable Long slotId) {

        return ResponseEntity.ok(
                parkingAllocationService.getAllocationsBySlot(slotId));
    }


    @PutMapping("/{allocationId}/cancel")
    public ResponseEntity<ParkingAllocation> cancelAllocation(
            @PathVariable Long allocationId) {

        return ResponseEntity.ok(
                parkingAllocationService.cancelAllocation(allocationId));
    }
}
