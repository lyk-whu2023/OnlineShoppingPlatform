package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.ShipmentDTO;
import org.example.onlineshoppingplatform.service.ShipmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/{orderId}")
    public ShipmentDTO get(@PathVariable Long orderId) {
        return shipmentService.getByOrderId(orderId);
    }

    @PutMapping("/{orderId}")
    public ShipmentDTO upsert(@PathVariable Long orderId, @RequestBody ShipmentDTO dto) {
        dto.setOrderId(orderId);
        return shipmentService.upsert(dto);
    }
}

