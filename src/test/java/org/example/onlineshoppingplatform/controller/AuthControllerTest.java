package org.example.onlineshoppingplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.onlineshoppingplatform.dto.UserDTO;
import org.example.onlineshoppingplatform.security.JwtUtil;
import org.example.onlineshoppingplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    UserService userService = Mockito.mock(UserService.class);
    JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);

    @BeforeEach
    void setup() {
        AuthController controller = new AuthController(userService, jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void loginRegister() throws Exception {
        UserDTO user = new UserDTO();
        user.setId(10L);
        user.setUsername("u");
        user.setRole("USER");
        Mockito.when(userService.login("u","p")).thenReturn(user);
        Mockito.when(jwtUtil.createToken(10L,"u","USER")).thenReturn("t");
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"u\",\"password\":\"p\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("t"))
                .andExpect(jsonPath("$.user.id").value(10L));

        Mockito.when(userService.register("r","p2","13800000000")).thenReturn(user);
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"r\",\"password\":\"p2\",\"phone\":\"13800000000\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(10L));
    }
}
