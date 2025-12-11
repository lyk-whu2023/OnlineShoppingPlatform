package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO getByOrderId(Long orderId);
    PaymentDTO upsert(PaymentDTO dto);
    PaymentDTO updateStatus(Long orderId, String status);
}

