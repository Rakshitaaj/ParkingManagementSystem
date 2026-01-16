package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ey.dto.request.CustomerIdProofRequestDTO;
import com.ey.dto.request.VehicleRequestDTO;
import com.ey.dto.response.CustomerIdProofResponseDTO;
import com.ey.dto.response.ParkingLocationResponseDTO;
import com.ey.dto.response.VehicleResponseDTO;
import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.Vehicle;
import com.ey.mapper.CustomerIdProofMapper;
import com.ey.mapper.VehicleMapper;
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

        // DTO → Entity
        CustomerIdProof idProof =
                CustomerIdProofMapper.toEntity(request);

        // Service
        CustomerIdProof saved =
                customerService.addIdProof(customerId, idProof);

        // Entity → Response DTO
        return ResponseEntity.ok(
                CustomerIdProofMapper.toResponse(saved));
    }

    @GetMapping("/{customerId}/id-proof")
    public ResponseEntity<CustomerIdProofResponseDTO> getIdProof(
            @PathVariable Long customerId) {

        CustomerIdProof saved =
                customerService.getIdProof(customerId);

        return ResponseEntity.ok(
                CustomerIdProofMapper.toResponse(saved));
    }

    // ================= VEHICLE =================

    @PostMapping("/{customerId}/vehicles")
    public ResponseEntity<VehicleResponseDTO> addVehicle(
            @PathVariable Long customerId,
            @Valid @RequestBody VehicleRequestDTO request) {

        // DTO → Entity
        Vehicle vehicle = VehicleMapper.toEntity(request);

        // Service
        Vehicle saved =
                customerService.addVehicle(customerId, vehicle);

        // Entity → Response DTO
        return ResponseEntity.ok(
                VehicleMapper.toResponse(saved));
    }

    @GetMapping("/{customerId}/vehicles")
    public ResponseEntity<List<VehicleResponseDTO>> getVehicles(
            @PathVariable Long customerId) {

        List<Vehicle> vehicles =
                customerService.getVehiclesByCustomer(customerId);

        List<VehicleResponseDTO> response =
                vehicles.stream()
                        .map(VehicleMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // ================= PARKING SEARCH =================

    @GetMapping("/parking-locations")
    public ResponseEntity<List<ParkingLocationResponseDTO>> searchParking(
            @RequestParam String city) {

        List<ParkingLocation> locations =
                customerService.getParkingLocationsByCity(city);

        List<ParkingLocationResponseDTO> response =
                locations.stream().map(l -> {
                    ParkingLocationResponseDTO dto =
                            new ParkingLocationResponseDTO();
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
