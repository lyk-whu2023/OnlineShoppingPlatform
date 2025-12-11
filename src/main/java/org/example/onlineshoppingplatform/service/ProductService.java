package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.ProductDTO;
import org.example.onlineshoppingplatform.dto.ProductQueryDTO;

import java.util.List;

public interface ProductService {
    ProductDTO getById(Long id);
    List<ProductDTO> list(ProductQueryDTO query);
    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    List<String> listImages(Long productId);
}

