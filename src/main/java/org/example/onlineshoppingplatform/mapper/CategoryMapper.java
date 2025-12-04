package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("SELECT * FROM categories WHERE id=#{id}")
    Category findById(@Param("id") Long id);

    @Select("SELECT * FROM categories WHERE name=#{name}")
    Category findByName(@Param("name") String name);

    @Select("SELECT * FROM categories ORDER BY id")
    List<Category> findAll();

    @Insert("INSERT INTO categories(name) VALUES(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category c);

    @Update("UPDATE categories SET name=#{name} WHERE id=#{id}")
    int update(Category c);

    @Delete("DELETE FROM categories WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
