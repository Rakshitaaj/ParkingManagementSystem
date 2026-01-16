package com.ey.controller;

import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.Vehicle;
import com.ey.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    // Add ID proof
    @PostMapping("/{customerId}/id-proof")
    public ResponseEntity<CustomerIdProof> addIdProof(
            @PathVariable Long customerId,
            @RequestBody CustomerIdProof idProof) {

        return ResponseEntity.ok(customerService.addIdProof(customerId, idProof));
    }

    // Get ID proof
    @GetMapping("/{customerId}/id-proof")
    public ResponseEntity<CustomerIdProof> getIdProof(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(customerService.getIdProof(customerId));
    }


    // Add vehicle
    @PostMapping("/{customerId}/vehicles")
    public ResponseEntity<Vehicle> addVehicle(
            @PathVariable Long customerId,
            @RequestBody Vehicle vehicle) {

        return ResponseEntity.ok(customerService.addVehicle(customerId, vehicle));
    }

    // Get vehicles by customer
    @GetMapping("/{customerId}/vehicles")
    public ResponseEntity<List<Vehicle>> getVehiclesByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(customerService.getVehiclesByCustomer(customerId));
    }


    // Search parking locations by city
    @GetMapping("/parking-locations")
    public ResponseEntity<List<ParkingLocation>> getParkingLocationsByCity(
            @RequestParam String city) {

        return ResponseEntity.ok(customerService.getParkingLocationsByCity(city));
    }
}
