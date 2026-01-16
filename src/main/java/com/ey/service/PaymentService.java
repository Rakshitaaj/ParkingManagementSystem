package com.ey.service;

import com.ey.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment initiatePayment(Long allocationId, Double amount, String paymentMode);

    Payment updatePaymentStatus(Long paymentId, String status);

    Payment getPaymentByAllocation(Long allocationId);

    List<Payment> getAllPayments();
}
