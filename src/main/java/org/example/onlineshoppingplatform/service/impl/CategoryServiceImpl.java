package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.CategoryDTO;
import org.example.onlineshoppingplatform.entity.Category;
import org.example.onlineshoppingplatform.mapper.CategoryMapper;
import org.example.onlineshoppingplatform.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> list() {
        return categoryMapper.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        categoryMapper.insert(c);
        return toDTO(categoryMapper.findById(c.getId()));
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category c = categoryMapper.findById(id);
        if (c == null) return null;
        c.setName(dto.getName());
        categoryMapper.update(c);
        return toDTO(categoryMapper.findById(id));
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    private CategoryDTO toDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }
}
