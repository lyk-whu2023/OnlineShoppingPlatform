package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("SELECT * FROM products WHERE id=#{id}")
    Product findById(@Param("id") Long id);

    @Select("SELECT * FROM products ORDER BY sales DESC")
    List<Product> findAll();

    @Select("SELECT * FROM products WHERE category_id=#{categoryId}")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    @Select("SELECT * FROM products WHERE price BETWEEN #{min} AND #{max}")
    List<Product> findByPriceBetween(@Param("min") java.math.BigDecimal min,
                                     @Param("max") java.math.BigDecimal max);

    @Insert("INSERT INTO products(name,category_id,price,stock,description,sales) VALUES(#{name},#{categoryId},#{price},#{stock},#{description},#{sales})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product p);

    @Update("UPDATE products SET name=#{name}, category_id=#{categoryId}, price=#{price}, stock=#{stock}, description=#{description}, sales=#{sales} WHERE id=#{id}")
    int update(Product p);

    @Delete("DELETE FROM products WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
