package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.ProductDTO;
import org.example.onlineshoppingplatform.entity.Favorite;
import org.example.onlineshoppingplatform.entity.Product;
import org.example.onlineshoppingplatform.entity.ProductImage;
import org.example.onlineshoppingplatform.mapper.FavoriteMapper;
import org.example.onlineshoppingplatform.mapper.ProductImageMapper;
import org.example.onlineshoppingplatform.mapper.ProductMapper;
import org.example.onlineshoppingplatform.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper,
                               ProductMapper productMapper,
                               ProductImageMapper productImageMapper) {
        this.favoriteMapper = favoriteMapper;
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public void add(Long userId, Long productId) {
        int exists = favoriteMapper.exists(userId, productId);
        if (exists > 0) return;
        Favorite f = new Favorite();
        f.setUserId(userId);
        f.setProductId(productId);
        f.setCreatedAt(LocalDateTime.now());
        favoriteMapper.insert(f);
    }

    @Override
    public void remove(Long userId, Long productId) {
        favoriteMapper.delete(userId, productId);
    }

    @Override
    public List<ProductDTO> listProducts(Long userId) {
        List<Favorite> fs = favoriteMapper.listByUserId(userId);
        List<ProductDTO> list = new ArrayList<>();
        for (Favorite f : fs) {
            Product p = productMapper.findById(f.getProductId());
            if (p == null) continue;
            ProductDTO dto = new ProductDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setCategoryId(p.getCategoryId());
            dto.setPrice(p.getPrice());
            dto.setStock(p.getStock());
            dto.setDescription(p.getDescription());
            dto.setSales(p.getSales());
            List<ProductImage> imgs = productImageMapper.listByProductId(p.getId());
            dto.setImages(imgs.stream().map(i -> i.getImageUrl() != null ? i.getImageUrl() : i.getImageSeed()).collect(Collectors.toList()));
            list.add(dto);
        }
        return list;
    }

    @Override
    public boolean isFavorite(Long userId, Long productId) {
        return favoriteMapper.exists(userId, productId) > 0;
    }
}
