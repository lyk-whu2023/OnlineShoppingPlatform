package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.ShipmentDTO;
import org.example.onlineshoppingplatform.entity.Shipment;
import org.example.onlineshoppingplatform.mapper.ShipmentMapper;
import org.example.onlineshoppingplatform.service.ShipmentService;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentMapper shipmentMapper;

    public ShipmentServiceImpl(ShipmentMapper shipmentMapper) {
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    public ShipmentDTO getByOrderId(Long orderId) {
        Shipment s = shipmentMapper.findByOrderId(orderId);
        if (s == null) return null;
        return toDTO(s);
    }

    @Override
    public ShipmentDTO upsert(ShipmentDTO dto) {
        Shipment existing = shipmentMapper.findByOrderId(dto.getOrderId());
        if (existing == null) {
            Shipment s = toEntity(dto);
            shipmentMapper.insert(s);
        } else {
            existing.setCarrier(dto.getCarrier());
            existing.setTrackingNo(dto.getTrackingNo());
            existing.setStatus(dto.getStatus());
            existing.setShippedAt(dto.getShippedAt());
            existing.setDeliveredAt(dto.getDeliveredAt());
            shipmentMapper.update(existing);
        }
        return getByOrderId(dto.getOrderId());
    }

    @Override
    public ShipmentDTO updateStatus(Long orderId, String status) {
        shipmentMapper.updateStatus(orderId, status, null, null);
        return getByOrderId(orderId);
    }

    private ShipmentDTO toDTO(Shipment s) {
        ShipmentDTO dto = new ShipmentDTO();
        dto.setOrderId(s.getOrderId());
        dto.setCarrier(s.getCarrier());
        dto.setTrackingNo(s.getTrackingNo());
        dto.setStatus(s.getStatus());
        dto.setShippedAt(s.getShippedAt());
        dto.setDeliveredAt(s.getDeliveredAt());
        return dto;
    }

    private Shipment toEntity(ShipmentDTO dto) {
        Shipment s = new Shipment();
        s.setOrderId(dto.getOrderId());
        s.setCarrier(dto.getCarrier());
        s.setTrackingNo(dto.getTrackingNo());
        s.setStatus(dto.getStatus());
        s.setShippedAt(dto.getShippedAt());
        s.setDeliveredAt(dto.getDeliveredAt());
        return s;
    }
}

