package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.ProductImage;

import java.util.List;

@Mapper
public interface ProductImageMapper {
    @Select("SELECT * FROM product_images WHERE product_id=#{productId} ORDER BY sort_order")
    List<ProductImage> listByProductId(@Param("productId") Long productId);

    @Insert("INSERT INTO product_images(product_id,image_url,image_seed,sort_order) VALUES(#{productId},#{imageUrl},#{imageSeed},#{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProductImage img);

    @Delete("DELETE FROM product_images WHERE product_id=#{productId}")
    int deleteByProductId(@Param("productId") Long productId);
}
