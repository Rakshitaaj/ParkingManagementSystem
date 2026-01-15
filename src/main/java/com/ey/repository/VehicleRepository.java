package com.ey.repository;

import com.ey.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByCustomerUserId(Long customerId);

    boolean existsByVehicleNumber(String vehicleNumber);
}
