package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.ProductDTO;
import org.example.onlineshoppingplatform.dto.ProductQueryDTO;
import org.example.onlineshoppingplatform.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Map<String, Object> list(ProductQueryDTO query) {
        List<ProductDTO> full = productService.list(query);
        Map<String, Object> resp = new HashMap<>();
        int page = query != null && query.getPage() != null ? query.getPage() : 1;
        int size = query != null && query.getSize() != null ? query.getSize() : 100;
        int from = Math.min((page - 1) * size, full.size());
        int to = Math.min(from + size, full.size());
        resp.put("list", full.subList(from, to));
        resp.put("total", full.size());
        resp.put("page", page);
        resp.put("size", size);
        return resp;
    }

    @GetMapping("/{id}")
    public ProductDTO get(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/{id}/images")
    public List<String> images(@PathVariable Long id) {
        return productService.listImages(id);
    }

    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return productService.create(dto);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
