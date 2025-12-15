package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.ProductDTO;

import java.util.List;

public interface FavoriteService {
    void add(Long userId, Long productId);
    void remove(Long userId, Long productId);
    List<ProductDTO> listProducts(Long userId);
    boolean isFavorite(Long userId, Long productId);
}
