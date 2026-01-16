package com.ey.service.impl;

import com.ey.entity.ParkingAllocation;
import com.ey.entity.Payment;
import com.ey.exception.ConflictException;
import com.ey.exception.ResourceNotFoundException;
import com.ey.repository.ParkingAllocationRepository;
import com.ey.repository.PaymentRepository;
import com.ey.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ParkingAllocationRepository allocationRepository;

    @Override
    public Payment initiatePayment(Long allocationId, Double amount, String paymentMode) {

        ParkingAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parking allocation not found with id " + allocationId));

        if (paymentRepository.findByAllocationAllocationId(allocationId).isPresent()) {
            throw new ConflictException("Payment already exists for this allocation");
        }

        Payment payment = new Payment();
        payment.setAllocation(allocation);
        payment.setAmount(amount);
        payment.setPaymentMode(paymentMode);
        payment.setPaymentStatus("PENDING");

        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, String status) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found with id " + paymentId));

        payment.setPaymentStatus(status);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByAllocation(Long allocationId) {

        return paymentRepository.findByAllocationAllocationId(allocationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found for allocation id " + allocationId));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
