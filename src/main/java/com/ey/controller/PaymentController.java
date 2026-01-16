package com.ey.controller;

import com.ey.entity.Payment;
import com.ey.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/initiate")
    public ResponseEntity<Payment> initiatePayment(
            @RequestParam Long allocationId,
            @RequestParam Double amount,
            @RequestParam String paymentMode) {

        return ResponseEntity.ok(
                paymentService.initiatePayment(allocationId, amount, paymentMode));
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
