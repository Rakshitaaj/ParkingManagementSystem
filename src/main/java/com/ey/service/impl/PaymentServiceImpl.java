package com.ey.service.impl;

import com.ey.entity.ParkingAllocation;
import com.ey.entity.Payment;
import com.ey.exception.ConflictException;
import com.ey.exception.ResourceNotFoundException;
import com.ey.repository.ParkingAllocationRepository;
import com.ey.repository.PaymentRepository;
import com.ey.service.PaymentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger =LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ParkingAllocationRepository allocationRepository;

    @Override
    public Payment initiatePayment(Long allocationId, Double amount, String paymentMode) {

        logger.info("Initiate payment request for allocationId {}, amount {}, mode {}",allocationId, amount, paymentMode);

        
        ParkingAllocation allocation =allocationRepository.findById(allocationId).orElseThrow(() -> {
                            logger.warn("Parking allocation not found with id {}",allocationId);
                            return new ResourceNotFoundException("Parking allocation not found with id " + allocationId);
                        });

        if (paymentRepository.findByAllocationAllocationId(allocationId).isPresent()) {
            logger.warn("Duplicate payment attempt for allocationId {}",allocationId);
            throw new ConflictException("Payment already exists for this allocation");
        }

        
        Payment payment = new Payment();
        payment.setAllocation(allocation);
       
        payment.setAmount(amount);
        payment.setPaymentMode(paymentMode);
        payment.setPaymentStatus("PENDING");

        Payment saved = paymentRepository.save(payment);

        logger.info("Payment initiated successfully with paymentId {}",saved.getPaymentId());

        return saved;
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, String status) {

        logger.info("Update payment status request for paymentId {}, status {}",paymentId, status);

        
        
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> {
                            logger.warn("Payment not found with id {}", paymentId);
                            return new ResourceNotFoundException("Payment not found with id " + paymentId);
                        });

        payment.setPaymentStatus(status);
        
        
        Payment updated = paymentRepository.save(payment);

        logger.info("Payment {} status updated to {}", paymentId, status);
        return updated;
    }

    
    @Override
    public Payment getPaymentByAllocation(Long allocationId) {

        logger.info("Fetch payment by allocationId {}", allocationId);

        
        return paymentRepository.findByAllocationAllocationId(allocationId).orElseThrow(() -> {
                    logger.warn("Payment not found for allocationId {}", allocationId);
                    return new ResourceNotFoundException("Payment not found for allocation id " + allocationId);
                });
    }

    
    @Override
    public List<Payment> getAllPayments() {

        logger.info("Fetch all payments");
        return paymentRepository.findAll();
    }
    
    
    @Override
    public Payment getPaymentById(Long paymentId) {

    	
        logger.info("Fetch payment by paymentId {}", paymentId);

        return paymentRepository.findById(paymentId).orElseThrow(() -> {
                    logger.warn("Payment not found with id {}", paymentId);
                    return new ResourceNotFoundException("Payment not found");
                });
    }
    
    
    
    @Override
    public List<Payment> getPaymentsByCustomer(Long customerId) {

        logger.info("Fetch payments for customerId {}", customerId);
        return paymentRepository.findByAllocationCustomerUserId(customerId);
    }
    
    @Override
    public List<Payment> getPaymentsByStatus(String status) {

        logger.info("Fetch payments with status {}", status);
        return paymentRepository.findByPaymentStatus(status);
    }
}
