package com.ey.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByAllocationAllocationId(Long allocationId);
    List<Payment> findByAllocationCustomerUserId(Long customerId);
    List<Payment> findByPaymentStatus(String status);

}
