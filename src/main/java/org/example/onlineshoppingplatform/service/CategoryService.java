package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> list();
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO update(Long id, CategoryDTO dto);
    void delete(Long id);
}

