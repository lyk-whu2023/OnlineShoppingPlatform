package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    List<CartItemDTO> getCartItems(Long userId);
    List<CartItemDTO> addItem(Long userId, CartItemDTO dto);
    List<CartItemDTO> updateItemQty(Long userId, Long productId, Integer qty);
    List<CartItemDTO> removeItem(Long userId, Long productId);
    void clear(Long userId);
}

