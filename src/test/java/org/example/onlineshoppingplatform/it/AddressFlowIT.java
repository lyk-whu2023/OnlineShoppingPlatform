package org.example.onlineshoppingplatform.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AddressFlowIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private String json(Object o) throws Exception { return objectMapper.writeValueAsString(o); }

    @Test
    void endToEndAddressCrud() throws Exception {
        Map<String, String> reg = new HashMap<>();
        reg.put("username", "it-user");
        reg.put("password", "123456");
        reg.put("phone", "13800000000");
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json(reg)))
                .andExpect(status().isOk());

        Map<String, String> login = new HashMap<>();
        login.put("username", "it-user");
        login.put("password", "123456");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(json(login)))
                .andExpect(status().isOk())
                .andReturn();
        Map<?,?> loginResp = objectMapper.readValue(loginResult.getResponse().getContentAsByteArray(), Map.class);
        String token = (String) loginResp.get("token");
        assertNotNull(token);
        String auth = "Bearer " + token;

        Map<String, Object> a1 = new HashMap<>();
        a1.put("name", "张三");
        a1.put("phone", "13800000001");
        a1.put("detail", "XX路1号");
        a1.put("isDefault", true);
        MvcResult createRes = mockMvc.perform(post("/api/addresses").header("Authorization", auth).contentType(MediaType.APPLICATION_JSON).content(json(a1)))
                .andExpect(status().isOk())
                .andReturn();
        Map<?,?> created = objectMapper.readValue(createRes.getResponse().getContentAsByteArray(), Map.class);
        Number addrId = (Number) created.get("id");
        assertNotNull(addrId);
        assertEquals(Boolean.TRUE, created.get("isDefault"));

        Map<String, Object> upd = new HashMap<>();
        upd.put("name", "李四");
        upd.put("phone", "13800000002");
        upd.put("detail", "YY路2号");
        upd.put("isDefault", false);
        MvcResult updRes = mockMvc.perform(put("/api/addresses/" + addrId.longValue()).header("Authorization", auth).contentType(MediaType.APPLICATION_JSON).content(json(upd)))
                .andExpect(status().isOk())
                .andReturn();
        Map<?,?> updated = objectMapper.readValue(updRes.getResponse().getContentAsByteArray(), Map.class);
        assertEquals("李四", updated.get("name"));
        assertEquals(Boolean.FALSE, updated.get("isDefault"));

        MvcResult listRes = mockMvc.perform(get("/api/addresses").header("Authorization", auth))
                .andExpect(status().isOk())
                .andReturn();
        List<?> list = objectMapper.readValue(listRes.getResponse().getContentAsByteArray(), List.class);
        assertFalse(list.isEmpty());

        mockMvc.perform(delete("/api/addresses/" + addrId.longValue()).header("Authorization", auth))
                .andExpect(status().isOk());

        MvcResult listRes2 = mockMvc.perform(get("/api/addresses").header("Authorization", auth))
                .andExpect(status().isOk())
                .andReturn();
        List<?> list2 = objectMapper.readValue(listRes2.getResponse().getContentAsByteArray(), List.class);
        assertTrue(list2.isEmpty());
    }
}
