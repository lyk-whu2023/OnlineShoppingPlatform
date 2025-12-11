package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.UserDTO;
import org.example.onlineshoppingplatform.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDTO me(@RequestAttribute("userId") Long userId) {
        return userService.getById(userId);
    }

    @GetMapping
    public List<UserDTO> list() {
        return userService.list();
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        userService.updateStatus(id, body.get("status"));
    }

    @PatchMapping("/me")
    public UserDTO updateMe(@RequestAttribute("userId") Long userId, @RequestBody java.util.Map<String, String> body) {
        return userService.updateMe(userId, body.get("phone"));
    }

    @PutMapping("/me")
    public UserDTO updateMePut(@RequestAttribute("userId") Long userId, @RequestBody java.util.Map<String, String> body) {
        return userService.updateMe(userId, body.get("phone"));
    }

    @PostMapping("/me")
    public UserDTO updateMePost(@RequestAttribute("userId") Long userId, @RequestBody java.util.Map<String, String> body) {
        return userService.updateMe(userId, body.get("phone"));
    }

    @PostMapping("/me/password")
    public void changePassword(@RequestAttribute("userId") Long userId, @RequestBody java.util.Map<String, String> body) {
        userService.changePassword(userId, body.get("oldPassword"), body.get("newPassword"));
    }
}
