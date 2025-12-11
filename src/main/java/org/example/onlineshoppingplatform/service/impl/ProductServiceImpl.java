package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.ProductDTO;
import org.example.onlineshoppingplatform.dto.ProductQueryDTO;
import org.example.onlineshoppingplatform.entity.Category;
import org.example.onlineshoppingplatform.entity.Product;
import org.example.onlineshoppingplatform.entity.ProductImage;
import org.example.onlineshoppingplatform.mapper.CategoryMapper;
import org.example.onlineshoppingplatform.mapper.ProductImageMapper;
import org.example.onlineshoppingplatform.mapper.ProductMapper;
import org.example.onlineshoppingplatform.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ProductImageMapper productImageMapper;

    public ProductServiceImpl(ProductMapper productMapper,
                              CategoryMapper categoryMapper,
                              ProductImageMapper productImageMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public ProductDTO getById(Long id) {
        Product p = productMapper.findById(id);
        if (p == null) return null;
        ProductDTO dto = toDTO(p);
        List<ProductImage> imgs = productImageMapper.listByProductId(id);
        dto.setImages(imgs.stream()
                .map(i -> i.getImageUrl() != null ? i.getImageUrl() : i.getImageSeed())
                .collect(Collectors.toList()));
        Category c = categoryMapper.findById(p.getCategoryId());
        dto.setCategoryName(c != null ? c.getName() : null);
        return dto;
    }

    @Override
    public List<ProductDTO> list(ProductQueryDTO query) {
        List<Product> list;
        if (query != null && query.getCategoryId() != null) {
            list = productMapper.findByCategoryId(query.getCategoryId());
        } else if (query != null && query.getMinPrice() != null && query.getMaxPrice() != null) {
            list = productMapper.findByPriceBetween(query.getMinPrice(), query.getMaxPrice());
        } else {
            list = productMapper.findAll();
        }
        if (query != null && query.getKeyword() != null && !query.getKeyword().trim().isEmpty()) {
            String kw = query.getKeyword().trim().toLowerCase();
            list = list.stream().filter(p -> {
                String n = p.getName() != null ? p.getName().toLowerCase() : "";
                String d = p.getDescription() != null ? p.getDescription().toLowerCase() : "";
                return n.contains(kw) || d.contains(kw);
            }).collect(Collectors.toList());
        }
        List<ProductDTO> dtos = new ArrayList<>();
        for (Product p : list) {
            ProductDTO dto = toDTO(p);
            Category c = categoryMapper.findById(p.getCategoryId());
            dto.setCategoryName(c != null ? c.getName() : null);
            dtos.add(dto);
        }
        if (query != null && query.getSort() != null) {
            if (Objects.equals(query.getSort(), "price_asc")) {
                dtos.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
            } else if (Objects.equals(query.getSort(), "price_desc")) {
                dtos.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
            } else if (Objects.equals(query.getSort(), "sales")) {
                dtos.sort((a, b) -> b.getSales().compareTo(a.getSales()));
            } else if (Objects.equals(query.getSort(), "relevance") && query.getKeyword() != null) {
                String kw = query.getKeyword().trim().toLowerCase();
                dtos.sort((a, b) -> score(b, kw) - score(a, kw));
            }
        }
        return dtos;
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        Product p = toEntity(dto);
        if (p.getSales() == null) p.setSales(0);
        if (p.getStock() == null) p.setStock(0);
        productMapper.insert(p);
        dto.setId(p.getId());
        return getById(p.getId());
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Product existing = productMapper.findById(id);
        if (existing == null) return null;
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getCategoryId() != null) existing.setCategoryId(dto.getCategoryId());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getStock() != null) existing.setStock(dto.getStock());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getSales() != null) existing.setSales(dto.getSales());
        productMapper.update(existing);
        return getById(id);
    }

    @Override
    public void delete(Long id) {
        productImageMapper.deleteByProductId(id);
        productMapper.deleteById(id);
    }

    @Override
    public List<String> listImages(Long productId) {
        List<ProductImage> imgs = productImageMapper.listByProductId(productId);
        return imgs.stream()
                .map(i -> i.getImageUrl() != null ? i.getImageUrl() : i.getImageSeed())
                .collect(Collectors.toList());
    }

    private ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setCategoryId(p.getCategoryId());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setDescription(p.getDescription());
        dto.setSales(p.getSales());
        return dto;
    }

    private int score(ProductDTO dto, String kw) {
        int s = 0;
        String n = dto.getName() != null ? dto.getName().toLowerCase() : "";
        String d = dto.getDescription() != null ? dto.getDescription().toLowerCase() : "";
        if (n.contains(kw)) s += 2;
        if (d.contains(kw)) s += 1;
        return s;
    }

    private Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setCategoryId(dto.getCategoryId());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setDescription(dto.getDescription());
        p.setSales(dto.getSales());
        return p;
    }
}
