package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.OrderDTO;
import org.example.onlineshoppingplatform.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDTO create(@RequestAttribute("userId") Long userId, @RequestBody java.util.Map<String, Long> body) {
        Long addressId = body.get("addressId");
        return orderService.create(userId, addressId);
    }
    
    @PostMapping("/batch")
    public OrderDTO createBatch(@RequestAttribute("userId") Long userId, @RequestBody java.util.Map<String, Object> body) {
        Object a = body.get("addressId");
        Long addressId = a instanceof Number ? ((Number)a).longValue() : null;
        java.util.List<java.util.Map<String, Object>> items = (java.util.List<java.util.Map<String, Object>>) body.get("items");
        java.util.List<org.example.onlineshoppingplatform.dto.CartItemDTO> dtos = new java.util.ArrayList<>();
        if (items != null) {
            for (var it : items) {
                Object pid = it.get("productId");
                Object qty = it.get("qty");
                if (pid instanceof Number && qty instanceof Number) {
                    org.example.onlineshoppingplatform.dto.CartItemDTO dto = new org.example.onlineshoppingplatform.dto.CartItemDTO();
                    dto.setProductId(((Number)pid).longValue());
                    dto.setQty(((Number)qty).intValue());
                    dtos.add(dto);
                }
            }
        }
        return orderService.createFromItems(userId, addressId, dtos);
    }

    @GetMapping("/{id}")
    public OrderDTO get(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @GetMapping
    public List<OrderDTO> list(@RequestParam(required = false) Long userId,
                               @RequestParam(required = false) String status) {
        if (userId != null) return orderService.listByUser(userId);
        if (status != null) return orderService.listByStatus(status);
        return orderService.listAll();
    }

    @GetMapping("/admin")
    public Map<String, Object> adminList(@RequestAttribute("role") String role,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {
        if (role == null || !"admin".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无管理员权限");
        }
        List<OrderDTO> full;
        if (status != null && !status.isEmpty()) full = orderService.listByStatus(status);
        else full = orderService.listAll();
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            full = full.stream().filter(o -> {
                String idStr = o.getId() != null ? String.valueOf(o.getId()) : "";
                String uidStr = o.getUserId() != null ? String.valueOf(o.getUserId()) : "";
                return idStr.contains(kw) || uidStr.contains(kw);
            }).toList();
        }
        int p = page != null ? page : 1;
        int s = size != null ? size : 100;
        int from = Math.min((p - 1) * s, full.size());
        int to = Math.min(from + s, full.size());
        Map<String, Object> resp = new HashMap<>();
        resp.put("list", full.subList(from, to));
        resp.put("total", full.size());
        resp.put("page", p);
        resp.put("size", s);
        return resp;
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        orderService.updateStatus(id, body.get("status"));
    }
}
