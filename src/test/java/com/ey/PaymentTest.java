package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ey.entity.Payment;

class PaymentTest {

    @Test
    void payment_basic_test() {

        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setAmount(100.0);
        payment.setPaymentMode("UPI");
        payment.setPaymentStatus("PENDING");

        assertNotNull(payment);
        assertEquals(100.0, payment.getAmount());
        assertEquals("UPI", payment.getPaymentMode());
        assertNotEquals("SUCCESS", payment.getPaymentStatus());
    }
}
