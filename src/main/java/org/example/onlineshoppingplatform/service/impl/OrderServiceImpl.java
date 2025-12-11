package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.OrderDTO;
import org.example.onlineshoppingplatform.dto.OrderItemDTO;
import org.example.onlineshoppingplatform.entity.Address;
import org.example.onlineshoppingplatform.entity.Order;
import org.example.onlineshoppingplatform.entity.OrderItem;
import org.example.onlineshoppingplatform.entity.Product;
import org.example.onlineshoppingplatform.mapper.AddressMapper;
import org.example.onlineshoppingplatform.mapper.CartItemMapper;
import org.example.onlineshoppingplatform.mapper.CartMapper;
import org.example.onlineshoppingplatform.mapper.OrderItemMapper;
import org.example.onlineshoppingplatform.mapper.OrderMapper;
import org.example.onlineshoppingplatform.mapper.ProductMapper;
import org.example.onlineshoppingplatform.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final AddressMapper addressMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper,
                            CartMapper cartMapper,
                            CartItemMapper cartItemMapper,
                            AddressMapper addressMapper,
                            ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.addressMapper = addressMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public OrderDTO create(Long userId, Long addressId) {
        Address addr = addressMapper.findById(addressId);
        if (addr == null || !addr.getUserId().equals(userId)) return null;
        var cart = cartMapper.findByUserId(userId);
        if (cart == null) return null;
        var items = cartItemMapper.listByCartId(cart.getId());
        if (items.isEmpty()) return null;
        BigDecimal amount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (var it : items) {
            Product p = productMapper.findById(it.getProductId());
            if (p == null) continue;
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(it.getQty()));
            amount = amount.add(line);
            OrderItem oi = new OrderItem();
            oi.setProductId(p.getId());
            oi.setName(p.getName());
            oi.setPrice(p.getPrice());
            oi.setQty(it.getQty());
            orderItems.add(oi);
            p.setStock(p.getStock() - it.getQty());
            p.setSales(p.getSales() + it.getQty());
            productMapper.update(p);
        }
        Order o = new Order();
        o.setUserId(userId);
        o.setAddressId(addressId);
        o.setAmount(amount);
        o.setStatus("CREATED");
        o.setConsigneeName(addr.getName());
        o.setConsigneePhone(addr.getPhone());
        o.setConsigneeDetail(addr.getDetail());
        orderMapper.insert(o);
        for (OrderItem oi : orderItems) {
            oi.setOrderId(o.getId());
            orderItemMapper.insert(oi);
        }
        cartItemMapper.clear(cart.getId());
        return getById(o.getId());
    }

    @Override
    public OrderDTO getById(Long id) {
        Order o = orderMapper.findById(id);
        if (o == null) return null;
        OrderDTO dto = new OrderDTO();
        dto.setId(o.getId());
        dto.setUserId(o.getUserId());
        dto.setAmount(o.getAmount());
        dto.setStatus(o.getStatus());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setConsigneeName(o.getConsigneeName());
        dto.setConsigneePhone(o.getConsigneePhone());
        dto.setConsigneeDetail(o.getConsigneeDetail());
        var items = orderItemMapper.listByOrderId(o.getId());
        dto.setItems(items.stream().map(this::toItemDTO).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public List<OrderDTO> listByUser(Long userId) {
        return orderMapper.listByUserId(userId).stream().map(o -> getById(o.getId())).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> listByStatus(String status) {
        return orderMapper.listByStatus(status).stream().map(o -> getById(o.getId())).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> listAll() {
        return orderMapper.listAll().stream().map(o -> getById(o.getId())).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Long id, String status) {
        orderMapper.updateStatus(id, status);
    }

    private OrderItemDTO toItemDTO(OrderItem it) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(it.getProductId());
        dto.setName(it.getName());
        dto.setPrice(it.getPrice());
        dto.setQty(it.getQty());
        return dto;
    }
}
