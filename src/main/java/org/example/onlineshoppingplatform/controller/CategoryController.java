package org.example.onlineshoppingplatform.controller;

import org.example.onlineshoppingplatform.dto.CategoryDTO;
import org.example.onlineshoppingplatform.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> list() {
        return categoryService.list();
    }

    @GetMapping("/admin")
    public Map<String, Object> adminList(@RequestAttribute("role") String role,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {
        if (role == null || !"admin".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无管理员权限");
        }
        List<CategoryDTO> full = categoryService.list();
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            full = full.stream().filter(c -> (c.getName() != null && c.getName().contains(kw))).toList();
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

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO dto) {
        return categoryService.create(dto);
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
