package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.CommentDTO;
import org.example.onlineshoppingplatform.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDTO> list(@RequestParam Long productId) {
        return commentService.listByProduct(productId);
    }

    @PostMapping
    public CommentDTO create(@RequestAttribute("userId") Long userId, @RequestBody Map<String, Object> body) {
        Long productId = body.get("productId") != null ? Long.valueOf(body.get("productId").toString()) : null;
        String content = body.get("content") != null ? body.get("content").toString() : null;
        Long parentId = body.get("parentId") != null ? Long.valueOf(body.get("parentId").toString()) : null;
        if (parentId != null) return commentService.addReply(userId, parentId, content);
        return commentService.addComment(userId, productId, content);
    }

    @GetMapping("/admin")
    public List<CommentDTO> adminList() {
        return commentService.listAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        commentService.updateStatus(id, body.get("status"));
    }
}
