package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO register(String username, String password, String phone);
    UserDTO login(String username, String password);
    UserDTO getById(Long id);
    List<UserDTO> list();
    void updateStatus(Long id, String status);
    UserDTO updateMe(Long id, String phone);
    void changePassword(Long id, String oldPassword, String newPassword);
}
