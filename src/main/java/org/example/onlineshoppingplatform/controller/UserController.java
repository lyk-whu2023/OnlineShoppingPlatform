package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.UserDTO;
import org.example.onlineshoppingplatform.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/admin")
    public Map<String, Object> adminList(@RequestAttribute("role") String role,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {
        if (role == null || !"admin".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无管理员权限");
        }
        List<UserDTO> full = userService.list();
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            full = full.stream().filter(u -> (u.getUsername() != null && u.getUsername().contains(kw))).toList();
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
