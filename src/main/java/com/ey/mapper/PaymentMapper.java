package com.ey.mapper;

import com.ey.dto.response.PaymentResponseDTO;
import com.ey.entity.Payment;

public class PaymentMapper {
    public static PaymentResponseDTO toResponse(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMode(payment.getPaymentMode());
        dto.setPaymentStatus(payment.getPaymentStatus());
        return dto;
    }
}
