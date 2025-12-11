package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.PaymentDTO;
import org.example.onlineshoppingplatform.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}")
    public PaymentDTO get(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }

    @PutMapping("/{orderId}")
    public PaymentDTO upsert(@PathVariable Long orderId, @RequestBody PaymentDTO dto) {
        dto.setOrderId(orderId);
        return paymentService.upsert(dto);
    }
}

