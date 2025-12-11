package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.ShipmentDTO;

public interface ShipmentService {
    ShipmentDTO getByOrderId(Long orderId);
    ShipmentDTO upsert(ShipmentDTO dto);
    ShipmentDTO updateStatus(Long orderId, String status);
}

