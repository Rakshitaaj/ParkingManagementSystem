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

    // ================= INITIATE PAYMENT =================

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(
            @Valid @RequestBody PaymentRequestDTO request) {

        Payment payment = paymentService.initiatePayment(
                request.getAllocationId(),
                request.getAmount(),
                request.getPaymentMode()
        );

        return ResponseEntity.ok(
                PaymentMapper.toResponse(payment));
    }

    // ================= UPDATE PAYMENT STATUS =================

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam String status) {

        Payment payment =
                paymentService.updatePaymentStatus(paymentId, status);

        return ResponseEntity.ok(
                PaymentMapper.toResponse(payment));
    }

    // ================= GET PAYMENT BY ALLOCATION =================

    @GetMapping("/allocation/{allocationId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByAllocation(
            @PathVariable Long allocationId) {

        Payment payment =
                paymentService.getPaymentByAllocation(allocationId);

        return ResponseEntity.ok(
                PaymentMapper.toResponse(payment));
    }

    // ================= GET ALL PAYMENTS (ADMIN) =================

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {

        List<Payment> payments =
                paymentService.getAllPayments();

        List<PaymentResponseDTO> response =
                payments.stream()
                        .map(PaymentMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }
}
