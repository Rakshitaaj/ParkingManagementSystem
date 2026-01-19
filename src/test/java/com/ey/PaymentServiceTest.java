package com.ey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ey.entity.Payment;
import com.ey.repository.PaymentRepository;
import com.ey.service.impl.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock PaymentRepository paymentRepository;
    @InjectMocks PaymentServiceImpl paymentService;

    @Test
    void updatePaymentStatus_success() {
        Payment payment = new Payment();
        payment.setPaymentId(1L);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));
        when(paymentRepository.save(any()))
                .thenReturn(payment);

        Payment result =
                paymentService.updatePaymentStatus(1L, "SUCCESS");

        assertEquals("SUCCESS", result.getPaymentStatus());
    }
}
