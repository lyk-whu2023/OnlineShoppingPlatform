package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.PaymentDTO;
import org.example.onlineshoppingplatform.entity.Payment;
import org.example.onlineshoppingplatform.mapper.PaymentMapper;
import org.example.onlineshoppingplatform.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentDTO getByOrderId(Long orderId) {
        Payment p = paymentMapper.findByOrderId(orderId);
        if (p == null) return null;
        return toDTO(p);
    }

    @Override
    public PaymentDTO upsert(PaymentDTO dto) {
        Payment existing = paymentMapper.findByOrderId(dto.getOrderId());
        if (existing == null) {
            Payment p = toEntity(dto);
            paymentMapper.insert(p);
        } else {
            existing.setAmount(dto.getAmount());
            existing.setMethod(dto.getMethod());
            existing.setStatus(dto.getStatus());
            existing.setPaidAt(dto.getPaidAt());
            paymentMapper.update(existing);
        }
        return getByOrderId(dto.getOrderId());
    }

    @Override
    public PaymentDTO updateStatus(Long orderId, String status) {
        paymentMapper.updateStatus(orderId, status, java.time.LocalDateTime.now());
        return getByOrderId(orderId);
    }

    private PaymentDTO toDTO(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setOrderId(p.getOrderId());
        dto.setAmount(p.getAmount());
        dto.setMethod(p.getMethod());
        dto.setStatus(p.getStatus());
        dto.setPaidAt(p.getPaidAt());
        return dto;
    }

    private Payment toEntity(PaymentDTO dto) {
        Payment p = new Payment();
        p.setOrderId(dto.getOrderId());
        p.setAmount(dto.getAmount());
        p.setMethod(dto.getMethod());
        p.setStatus(dto.getStatus());
        p.setPaidAt(dto.getPaidAt());
        return p;
    }
}

