package org.example.onlineshoppingplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.onlineshoppingplatform.dto.UserDTO;
import org.example.onlineshoppingplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    UserService userService = Mockito.mock(UserService.class);

    @BeforeEach
    void setup() {
        UserController controller = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void meAndUpdateAndChangePassword() throws Exception {
        UserDTO u = new UserDTO();
        u.setId(10L);
        u.setUsername("testuser");
        u.setPhone("13800000000");
        Mockito.when(userService.getById(10L)).thenReturn(u);
        mockMvc.perform(get("/api/users/me").requestAttr("userId", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        UserDTO u2 = new UserDTO();
        u2.setId(10L);
        u2.setUsername("testuser");
        u2.setPhone("13800000001");
        Mockito.when(userService.updateMe(10L, "13800000001")).thenReturn(u2);
        mockMvc.perform(patch("/api/users/me").requestAttr("userId", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"13800000001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("13800000001"));

        mockMvc.perform(post("/api/users/me/password").requestAttr("userId", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\":\"123456\",\"newPassword\":\"654321\"}"))
                .andExpect(status().isOk());
        Mockito.verify(userService).changePassword(10L, "123456", "654321");
    }
}
