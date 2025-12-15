package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.ProductDTO;
import org.example.onlineshoppingplatform.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<ProductDTO> list(@RequestAttribute("userId") Long userId) {
        return favoriteService.listProducts(userId);
    }

    @PostMapping
    public Map<String, Object> add(@RequestAttribute("userId") Long userId, @RequestBody Map<String, Long> body) {
        Long productId = body.get("productId");
        favoriteService.add(userId, productId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("list", favoriteService.listProducts(userId));
        return resp;
    }

    @DeleteMapping("/{productId}")
    public void remove(@RequestAttribute("userId") Long userId, @PathVariable Long productId) {
        favoriteService.remove(userId, productId);
    }

    @GetMapping("/check")
    public Map<String, Boolean> check(@RequestAttribute("userId") Long userId, @RequestParam Long productId) {
        boolean fav = favoriteService.isFavorite(userId, productId);
        Map<String, Boolean> resp = new HashMap<>();
        resp.put("favorite", fav);
        return resp;
    }
}
