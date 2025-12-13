package org.example.onlineshoppingplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.onlineshoppingplatform.dto.CartItemDTO;
import org.example.onlineshoppingplatform.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    CartService cartService = Mockito.mock(CartService.class);

    @BeforeEach
    void setup() {
        CartController controller = new CartController(cartService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void cartCrud() throws Exception {
        CartItemDTO item = new CartItemDTO();
        item.setProductId(100L);
        item.setQty(2);
        Mockito.when(cartService.getCartItems(10L)).thenReturn(List.of(item));
        mockMvc.perform(get("/api/cart").requestAttr("userId", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(100L))
                .andExpect(jsonPath("$[0].qty").value(2));

        CartItemDTO addReq = new CartItemDTO();
        addReq.setProductId(100L);
        addReq.setQty(3);
        Mockito.when(cartService.addItem(Mockito.eq(10L), Mockito.any())).thenReturn(List.of(addReq));
        mockMvc.perform(post("/api/cart/items").requestAttr("userId", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].qty").value(3));

        Mockito.when(cartService.updateItemQty(10L, 100L, 5)).thenReturn(List.of(addReq));
        mockMvc.perform(put("/api/cart/items/100").requestAttr("userId", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"qty\":5}"))
                .andExpect(status().isOk());

        Mockito.when(cartService.removeItem(10L, 100L)).thenReturn(List.of());
        mockMvc.perform(delete("/api/cart/items/100").requestAttr("userId", 10L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/cart").requestAttr("userId", 10L))
                .andExpect(status().isOk());
        Mockito.verify(cartService).clear(10L);
    }
}
