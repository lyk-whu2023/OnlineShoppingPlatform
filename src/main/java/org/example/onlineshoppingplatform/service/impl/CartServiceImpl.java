package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.CartItemDTO;
import org.example.onlineshoppingplatform.entity.Cart;
import org.example.onlineshoppingplatform.entity.CartItem;
import org.example.onlineshoppingplatform.mapper.CartItemMapper;
import org.example.onlineshoppingplatform.mapper.CartMapper;
import org.example.onlineshoppingplatform.mapper.ProductImageMapper;
import org.example.onlineshoppingplatform.mapper.ProductMapper;
import org.example.onlineshoppingplatform.entity.Product;
import org.example.onlineshoppingplatform.entity.ProductImage;
import org.example.onlineshoppingplatform.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;

    public CartServiceImpl(CartMapper cartMapper, CartItemMapper cartItemMapper, ProductMapper productMapper, ProductImageMapper productImageMapper) {
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
    }

    private Cart ensureCart(Long userId) {
        Cart c = cartMapper.findByUserId(userId);
        if (c == null) {
            c = new Cart();
            c.setUserId(userId);
            cartMapper.insert(c);
        }
        return c;
    }

    @Override
    public List<CartItemDTO> getCartItems(Long userId) {
        Cart c = ensureCart(userId);
        return cartItemMapper.listByCartId(c.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CartItemDTO> addItem(Long userId, CartItemDTO dto) {
        Cart c = ensureCart(userId);
        CartItem existing = cartItemMapper.findOne(c.getId(), dto.getProductId());
        if (existing == null) {
            CartItem it = new CartItem();
            it.setCartId(c.getId());
            it.setProductId(dto.getProductId());
            it.setQty(dto.getQty());
            cartItemMapper.insert(it);
        } else {
            cartItemMapper.updateQty(existing.getId(), dto.getQty());
        }
        return getCartItems(userId);
    }

    @Override
    @Transactional
    public List<CartItemDTO> updateItemQty(Long userId, Long productId, Integer qty) {
        Cart c = ensureCart(userId);
        CartItem existing = cartItemMapper.findOne(c.getId(), productId);
        if (existing != null) {
            cartItemMapper.updateQty(existing.getId(), qty);
        }
        return getCartItems(userId);
    }

    @Override
    @Transactional
    public List<CartItemDTO> removeItem(Long userId, Long productId) {
        Cart c = ensureCart(userId);
        cartItemMapper.deleteOne(c.getId(), productId);
        return getCartItems(userId);
    }

    @Override
    @Transactional
    public void clear(Long userId) {
        Cart c = ensureCart(userId);
        cartItemMapper.clear(c.getId());
    }

    private CartItemDTO toDTO(CartItem it) {
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(it.getProductId());
        dto.setQty(it.getQty());
        Product p = productMapper.findById(it.getProductId());
        if (p != null) {
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
        }
        java.util.List<ProductImage> imgs = productImageMapper.listByProductId(it.getProductId());
        if (imgs != null && !imgs.isEmpty()) {
            String v = imgs.get(0).getImageUrl() != null ? imgs.get(0).getImageUrl() : imgs.get(0).getImageSeed();
            dto.setImage(v);
        }
        return dto;
    }
}
