package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.PaymentRequestDTO;
import com.ey.dto.response.PaymentResponseDTO;
import com.ey.entity.Payment;
import com.ey.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(
            @Valid @RequestBody PaymentRequestDTO request) {

        Payment payment = paymentService.initiatePayment(
                request.getAllocationId(),
                request.getAmount(),
                request.getPaymentMode()
        );

        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setPaymentId(payment.getPaymentId());
        response.setAmount(payment.getAmount());
        response.setPaymentMode(payment.getPaymentMode());
        response.setPaymentStatus(payment.getPaymentStatus());

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{paymentId}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                paymentService.updatePaymentStatus(paymentId, status));
    }


    // Get payment by allocation
    @GetMapping("/allocation/{allocationId}")
    public ResponseEntity<Payment> getPaymentByAllocation(
            @PathVariable Long allocationId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByAllocation(allocationId));
    }

    // Get all payments (Admin)
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
