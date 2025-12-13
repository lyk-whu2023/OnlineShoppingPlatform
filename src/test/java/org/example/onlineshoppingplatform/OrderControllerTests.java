package org.example.onlineshoppingplatform;

import org.example.onlineshoppingplatform.controller.OrderController;
import org.example.onlineshoppingplatform.dto.CartItemDTO;
import org.example.onlineshoppingplatform.dto.OrderDTO;
import org.example.onlineshoppingplatform.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class OrderControllerTests {

    @Test
    void listAllOrdersReturnsData() {
        OrderService stub = new OrderService() {
            @Override
            public OrderDTO create(Long userId, Long addressId) { return null; }
            @Override
            public OrderDTO getById(Long id) { return null; }
            @Override
            public List<OrderDTO> listByUser(Long userId) { return List.of(); }
            @Override
            public List<OrderDTO> listByStatus(String status) { return List.of(); }
            @Override
            public List<OrderDTO> listAll() {
                OrderDTO dto = new OrderDTO();
                dto.setId(1L);
                dto.setUserId(2L);
                dto.setAmount(new BigDecimal("99.99"));
                dto.setStatus("CREATED");
                return List.of(dto);
            }
            @Override
            public void updateStatus(Long id, String status) {}
            @Override
            public OrderDTO createFromItems(Long userId, Long addressId, List<CartItemDTO> items) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'createFromItems'");
            }
        };

        OrderController controller = new OrderController(stub);
        List<OrderDTO> res = controller.list(null, null);
        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals(1L, res.get(0).getId());
        Assertions.assertEquals("CREATED", res.get(0).getStatus());
    }
}
