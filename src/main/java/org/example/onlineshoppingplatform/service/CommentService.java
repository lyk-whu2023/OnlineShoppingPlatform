package org.example.onlineshoppingplatform.service;

import org.example.onlineshoppingplatform.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO addComment(Long userId, Long productId, String content);
    CommentDTO addReply(Long userId, Long parentId, String content);
    List<CommentDTO> listByProduct(Long productId);
    List<CommentDTO> listAll();
    void delete(Long id);
    void updateStatus(Long id, String status);
}
