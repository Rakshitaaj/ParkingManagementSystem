package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.CustomerIdProofRequestDTO;
import com.ey.dto.request.VehicleRequestDTO;
import com.ey.dto.response.CustomerIdProofResponseDTO;
import com.ey.dto.response.ParkingLocationResponseDTO;
import com.ey.dto.response.VehicleResponseDTO;
import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.Vehicle;
import com.ey.enums.IdProofType;
import com.ey.exception.BadRequestException;
import com.ey.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ================= ID PROOF =================

    @PostMapping("/{customerId}/id-proof")
    public ResponseEntity<CustomerIdProofResponseDTO> addIdProof(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerIdProofRequestDTO request) {

        CustomerIdProof idProof = new CustomerIdProof();

        try {
            idProof.setIdProofType(
                    IdProofType.valueOf(request.getIdProofType().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid ID proof type");
        }

        idProof.setIdProofNumber(request.getIdProofNumber());

        CustomerIdProof saved = customerService.addIdProof(customerId, idProof);

        CustomerIdProofResponseDTO response = new CustomerIdProofResponseDTO();
        response.setId(saved.getId());
        response.setIdProofType(saved.getIdProofType().name());
        response.setIdProofNumber(saved.getIdProofNumber());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{customerId}/id-proof")
    public ResponseEntity<CustomerIdProofResponseDTO> getIdProof(
            @PathVariable Long customerId) {

        CustomerIdProof saved = customerService.getIdProof(customerId);

        CustomerIdProofResponseDTO response = new CustomerIdProofResponseDTO();
        response.setId(saved.getId());
        response.setIdProofType(saved.getIdProofType().name()); 
        response.setIdProofNumber(saved.getIdProofNumber());

        return ResponseEntity.ok(response);
    }


    // ================= VEHICLE =================

    @PostMapping("/{customerId}/vehicles")
    public ResponseEntity<VehicleResponseDTO> addVehicle(
            @PathVariable Long customerId,
            @Valid @RequestBody VehicleRequestDTO request) {

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleType(request.getVehicleType());

        Vehicle saved = customerService.addVehicle(customerId, vehicle);

        VehicleResponseDTO response = new VehicleResponseDTO();
        response.setVehicleId(saved.getVehicleId());
        response.setVehicleNumber(saved.getVehicleNumber());
        response.setVehicleType(saved.getVehicleType());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}/vehicles")
    public ResponseEntity<List<VehicleResponseDTO>> getVehicles(
            @PathVariable Long customerId) {

        List<Vehicle> vehicles = customerService.getVehiclesByCustomer(customerId);

        List<VehicleResponseDTO> response = vehicles.stream().map(v -> {
            VehicleResponseDTO dto = new VehicleResponseDTO();
            dto.setVehicleId(v.getVehicleId());
            dto.setVehicleNumber(v.getVehicleNumber());
            dto.setVehicleType(v.getVehicleType());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }

    // ================= PARKING SEARCH =================

    @GetMapping("/parking-locations")
    public ResponseEntity<List<ParkingLocationResponseDTO>> searchParking(
            @RequestParam String city) {

        List<ParkingLocation> locations =
                customerService.getParkingLocationsByCity(city);

        List<ParkingLocationResponseDTO> response = locations.stream().map(l -> {
            ParkingLocationResponseDTO dto = new ParkingLocationResponseDTO();
            dto.setLocationId(l.getLocationId());
            dto.setLocationName(l.getLocationName());
            dto.setAddress(l.getAddress());
            dto.setCity(l.getCity());
            dto.setActive(l.isActive());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }
}

