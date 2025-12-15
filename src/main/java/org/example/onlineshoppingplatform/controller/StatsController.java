package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.ProductDTO;
import org.example.onlineshoppingplatform.entity.Product;
import org.example.onlineshoppingplatform.mapper.OrderMapper;
import org.example.onlineshoppingplatform.mapper.ProductMapper;
import org.example.onlineshoppingplatform.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    public StatsController(ProductMapper productMapper, OrderMapper orderMapper, UserMapper userMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public Map<String, Object> dashboard() {
        List<Product> products = productMapper.findAll();
        int productCount = products.size();
        int userCount = userMapper.findAll().size();
        int orderCount = orderMapper.listAll().size();
        BigDecimal totalSales = orderMapper.listAll().stream()
                .map(o -> o.getAmount() != null ? o.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<Map<String, Object>> topProducts = products.stream()
                .sorted((a, b) -> b.getSales().compareTo(a.getSales()))
                .limit(5)
                .map(p -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", p.getId());
                    m.put("name", p.getName());
                    m.put("sales", p.getSales());
                    return m;
                })
                .collect(Collectors.toList());
        Map<String, Object> resp = new HashMap<>();
        resp.put("productCount", productCount);
        resp.put("userCount", userCount);
        resp.put("orderCount", orderCount);
        resp.put("totalSales", totalSales);
        resp.put("topProducts", topProducts);
        return resp;
    }
}
