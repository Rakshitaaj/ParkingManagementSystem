package com.ey.controller;

import java.time.LocalDateTime;
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
import com.ey.dto.response.ParkingSlotResponseDTO;
import com.ey.dto.response.VehicleResponseDTO;
import com.ey.entity.CustomerIdProof;
import com.ey.entity.ParkingLocation;
import com.ey.entity.Vehicle;
import com.ey.mapper.CustomerIdProofMapper;
import com.ey.mapper.ParkingSlotMapper;
import com.ey.mapper.VehicleMapper;
import com.ey.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/{customerId}/id-proof")
    public ResponseEntity<CustomerIdProofResponseDTO> addIdProof(@PathVariable Long customerId, @Valid @RequestBody CustomerIdProofRequestDTO request) {

        CustomerIdProof idProof =CustomerIdProofMapper.toEntity(request);
        CustomerIdProof saved =customerService.addIdProof(customerId, idProof);
        return ResponseEntity.ok(CustomerIdProofMapper.toResponse(saved));
    }

    @GetMapping("/{customerId}/id-proof")
    public ResponseEntity<CustomerIdProofResponseDTO> getIdProof(@PathVariable Long customerId) {
        CustomerIdProof saved =customerService.getIdProof(customerId);
        return ResponseEntity.ok(CustomerIdProofMapper.toResponse(saved));
    }

    @PostMapping("/{customerId}/vehicles")
    public ResponseEntity<VehicleResponseDTO> addVehicle(@PathVariable Long customerId,@Valid @RequestBody VehicleRequestDTO request) {        
        Vehicle vehicle = VehicleMapper.toEntity(request);
        Vehicle saved =customerService.addVehicle(customerId, vehicle);      
        return ResponseEntity.ok(VehicleMapper.toResponse(saved));
    }

    @GetMapping("/{customerId}/vehicles")
    public ResponseEntity<List<VehicleResponseDTO>> getVehicles(@PathVariable Long customerId) {

        List<Vehicle> vehicles =customerService.getVehiclesByCustomer(customerId);
        List<VehicleResponseDTO> response =vehicles.stream().map(VehicleMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/parking-locations")
    public ResponseEntity<List<ParkingLocationResponseDTO>> searchParking(@RequestParam String city) {

        List<ParkingLocation> locations =customerService.getParkingLocationsByCity(city);
        List<ParkingLocationResponseDTO> response =locations.stream().map(l -> {ParkingLocationResponseDTO dto =new ParkingLocationResponseDTO();
                    dto.setLocationId(l.getLocationId());
                    dto.setLocationName(l.getLocationName());
                    dto.setAddress(l.getAddress());
                    dto.setCity(l.getCity());
                    dto.setActive(l.isActive());
                    return dto;}).toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/locations/{locationId}/available-slots")
    public ResponseEntity<List<ParkingSlotResponseDTO>> getAvailableSlots(@PathVariable Long locationId,@RequestParam LocalDateTime startTime,@RequestParam LocalDateTime endTime) {

        return ResponseEntity.ok(customerService.getAvailableSlots(locationId, startTime, endTime).stream()
                        .map(ParkingSlotMapper::toResponse).toList());
    }

}
