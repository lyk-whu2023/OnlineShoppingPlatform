package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO create(Long userId, Long addressId);
    OrderDTO getById(Long id);
    List<OrderDTO> listByUser(Long userId);
    List<OrderDTO> listByStatus(String status);
    List<OrderDTO> listAll();
    void updateStatus(Long id, String status);
    OrderDTO createFromItems(Long userId, Long addressId, java.util.List<org.example.onlineshoppingplatform.dto.CartItemDTO> items);
}
