package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.UserDTO;
import org.example.onlineshoppingplatform.security.JwtUtil;
import org.example.onlineshoppingplatform.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        UserDTO user = userService.login(body.get("username"), body.get("password"));
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", user != null ? jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole()) : null);
        resp.put("user", user);
        return resp;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        UserDTO user = userService.register(body.get("username"), body.get("password"), body.get("phone"));
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", user != null ? jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole()) : null);
        resp.put("user", user);
        return resp;
    }

    @PostMapping("/logout")
    public void logout() {
    }
}
