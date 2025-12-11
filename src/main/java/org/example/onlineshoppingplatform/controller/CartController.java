package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.CartItemDTO;
import org.example.onlineshoppingplatform.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItemDTO> list(@RequestAttribute("userId") Long userId) {
        return cartService.getCartItems(userId);
    }

    @PostMapping("/items")
    public List<CartItemDTO> add(@RequestAttribute("userId") Long userId, @RequestBody CartItemDTO dto) {
        return cartService.addItem(userId, dto);
    }

    @PutMapping("/items/{productId}")
    public List<CartItemDTO> update(@RequestAttribute("userId") Long userId, @PathVariable Long productId, @RequestBody java.util.Map<String, Integer> body) {
        Integer qty = body.get("qty");
        return cartService.updateItemQty(userId, productId, qty);
    }

    @DeleteMapping("/items/{productId}")
    public List<CartItemDTO> remove(@RequestAttribute("userId") Long userId, @PathVariable Long productId) {
        return cartService.removeItem(userId, productId);
    }

    @DeleteMapping
    public void clear(@RequestAttribute("userId") Long userId) {
        cartService.clear(userId);
    }
}
