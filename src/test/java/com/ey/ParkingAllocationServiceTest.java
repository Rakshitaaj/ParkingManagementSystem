package com.ey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ey.entity.ParkingAllocation;
import com.ey.repository.ParkingAllocationRepository;
import com.ey.service.impl.ParkingAllocationServiceImpl;

@ExtendWith(MockitoExtension.class)
class ParkingAllocationServiceTest {

    @Mock ParkingAllocationRepository allocationRepository;
    @InjectMocks ParkingAllocationServiceImpl allocationService;

    @Test
    void cancelAllocation_success() {
        ParkingAllocation allocation = new ParkingAllocation();
        allocation.setAllocationId(1L);

        when(allocationRepository.findById(1L))
                .thenReturn(Optional.of(allocation));
        when(allocationRepository.save(any()))
                .thenReturn(allocation);

        ParkingAllocation result =
                allocationService.cancelAllocation(1L);

        assertEquals("CANCELLED", result.getStatus());
    }
}


