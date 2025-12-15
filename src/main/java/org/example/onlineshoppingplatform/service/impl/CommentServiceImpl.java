package org.example.onlineshoppingplatform.service.impl;

import org.example.onlineshoppingplatform.dto.CommentDTO;
import org.example.onlineshoppingplatform.entity.Comment;
import org.example.onlineshoppingplatform.mapper.CommentMapper;
import org.example.onlineshoppingplatform.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDTO addComment(Long userId, Long productId, String content) {
        Comment c = new Comment();
        c.setUserId(userId);
        c.setProductId(productId);
        c.setContent(content);
        c.setParentId(null);
        c.setStatus("visible");
        c.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(c);
        return toDTO(c);
    }

    @Override
    public CommentDTO addReply(Long userId, Long parentId, String content) {
        Comment parent = commentMapper.findById(parentId);
        if (parent == null) return null;
        Comment c = new Comment();
        c.setUserId(userId);
        c.setProductId(parent.getProductId());
        c.setContent(content);
        c.setParentId(parentId);
        c.setStatus("visible");
        c.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(c);
        return toDTO(c);
    }

    @Override
    public List<CommentDTO> listByProduct(Long productId) {
        List<Comment> tops = commentMapper.listTopByProductId(productId);
        List<CommentDTO> dtos = new ArrayList<>();
        for (Comment c : tops) {
            CommentDTO dto = toDTO(c);
            List<Comment> replies = commentMapper.listReplies(c.getId());
            dto.setReplies(replies.stream().map(this::toDTO).collect(Collectors.toList()));
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<CommentDTO> listAll() {
        return commentMapper.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        commentMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        commentMapper.updateStatus(id, status);
    }

    private CommentDTO toDTO(Comment c) {
        CommentDTO dto = new CommentDTO();
        dto.setId(c.getId());
        dto.setProductId(c.getProductId());
        dto.setUserId(c.getUserId());
        dto.setContent(c.getContent());
        dto.setParentId(c.getParentId());
        dto.setStatus(c.getStatus());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}
