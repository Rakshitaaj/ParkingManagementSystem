package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ey.dto.request.PaymentRequestDTO;
import com.ey.dto.response.PaymentResponseDTO;
import com.ey.entity.Payment;
import com.ey.mapper.PaymentMapper;
import com.ey.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@Valid @RequestBody PaymentRequestDTO request) {
        Payment payment = paymentService.initiatePayment(request.getAllocationId(),request.getAmount(),request.getPaymentMode());
        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }
    

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(@PathVariable Long paymentId,@RequestParam String status) {
        Payment payment =paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }
    
    
    

    @GetMapping("/allocation/{allocationId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByAllocation(@PathVariable Long allocationId) {
        Payment payment =paymentService.getPaymentByAllocation(allocationId);
        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }
    
    
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<Payment> payments =paymentService.getAllPayments();
        List<PaymentResponseDTO> response =payments.stream().map(PaymentMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }
    
    
    
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(PaymentMapper.toResponse(paymentService.getPaymentById(paymentId)));
    }
    
    
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(customerId).stream().map(PaymentMapper::toResponse).toList());
    }
    
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status).stream().map(PaymentMapper::toResponse).toList());
    }
}
