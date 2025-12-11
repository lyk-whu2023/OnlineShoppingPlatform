package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.UserDTO;
import org.example.onlineshoppingplatform.entity.User;
import org.example.onlineshoppingplatform.mapper.UserMapper;
import org.example.onlineshoppingplatform.service.UserService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO register(String username, String password, String phone) {
        User exists = userMapper.findByUsername(username);
        if (exists != null) return null;
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(hash(password));
        u.setRole("user");
        u.setPhone(phone);
        u.setStatus("enabled");
        userMapper.insert(u);
        return toDTO(userMapper.findById(u.getId()));
    }

    @Override
    public UserDTO login(String username, String password) {
        User u = userMapper.findByUsername(username);
        if (u == null) {
            // 如果用户名不存在，尝试按手机号登录
            u = userMapper.findByPhone(username);
            if (u == null) return null;
        }
        if (!u.getPasswordHash().equals(hash(password))) return null;
        return toDTO(u);
    }

    @Override
    public UserDTO getById(Long id) {
        User u = userMapper.findById(id);
        if (u == null) return null;
        return toDTO(u);
    }

    @Override
    public List<UserDTO> list() {
        return userMapper.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Long id, String status) {
        userMapper.updateStatus(id, status);
    }

    @Override
    public UserDTO updateMe(Long id, String phone) {
        User u = userMapper.findById(id);
        if (u == null) return null;
        u.setPhone(phone);
        userMapper.update(u);
        return toDTO(userMapper.findById(id));
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User u = userMapper.findById(id);
        if (u == null) return;
        if (!u.getPasswordHash().equals(hash(oldPassword))) return;
        userMapper.updatePassword(id, hash(newPassword));
    }

    private String hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) {
                sb.append(String.format("%02x", x));
            }
            return sb.toString();
        } catch (Exception e) {
            return s;
        }
    }

    private UserDTO toDTO(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setRole(u.getRole());
        dto.setPhone(u.getPhone());
        dto.setStatus(u.getStatus());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }
}
