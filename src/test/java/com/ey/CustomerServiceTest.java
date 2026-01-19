package com.ey;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ey.entity.User;
import com.ey.entity.Vehicle;
import com.ey.repository.CustomerIdProofRepository;
import com.ey.repository.UserRepository;
import com.ey.repository.VehicleRepository;
import com.ey.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock UserRepository userRepository;
    @Mock VehicleRepository vehicleRepository;
    @Mock CustomerIdProofRepository idProofRepository;

    @InjectMocks CustomerServiceImpl customerService;

    @Test
    void addVehicle_success() {
        User customer = new User();
        customer.setUserId(1L);

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("TN01AB1234");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(customer));
        when(vehicleRepository.existsByVehicleNumber(any()))
                .thenReturn(false);
        when(vehicleRepository.save(any()))
                .thenReturn(vehicle);

        assertNotNull(customerService.addVehicle(1L, vehicle));
    }
}
