package org.example.onlineshoppingplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.onlineshoppingplatform.dto.AddressDTO;
import org.example.onlineshoppingplatform.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AddressControllerTest {
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    AddressService addressService = Mockito.mock(AddressService.class);

    @BeforeEach
    void setup() {
        AddressController controller = new AddressController(addressService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listAddresses() throws Exception {
        AddressDTO a = new AddressDTO();
        a.setId(1L);
        a.setUserId(10L);
        a.setName("张三");
        a.setPhone("13800000001");
        a.setDetail("XX路1号");
        a.setIsDefault(true);
        Mockito.when(addressService.listByUser(10L)).thenReturn(List.of(a));
        mockMvc.perform(get("/api/addresses").requestAttr("userId", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("张三"))
                .andExpect(jsonPath("$[0].isDefault").value(true));
    }

    @Test
    void createUpdateDeleteAddress() throws Exception {
        AddressDTO createReq = new AddressDTO();
        createReq.setName("李四");
        createReq.setPhone("13800000002");
        createReq.setDetail("YY路2号");
        createReq.setIsDefault(false);
        AddressDTO created = new AddressDTO();
        created.setId(2L);
        created.setUserId(10L);
        created.setName(createReq.getName());
        created.setPhone(createReq.getPhone());
        created.setDetail(createReq.getDetail());
        created.setIsDefault(false);
        Mockito.when(addressService.create(Mockito.any())).thenReturn(created);
        mockMvc.perform(post("/api/addresses").requestAttr("userId", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("李四"));

        AddressDTO updated = new AddressDTO();
        updated.setId(2L);
        updated.setUserId(10L);
        updated.setName("王五");
        updated.setPhone("13800000003");
        updated.setDetail("ZZ路3号");
        updated.setIsDefault(true);
        Mockito.when(addressService.update(Mockito.eq(2L), Mockito.any())).thenReturn(updated);
        mockMvc.perform(put("/api/addresses/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("王五"))
                .andExpect(jsonPath("$.isDefault").value(true));

        mockMvc.perform(delete("/api/addresses/2"))
                .andExpect(status().isOk());
        Mockito.verify(addressService).delete(2L);
    }
}
