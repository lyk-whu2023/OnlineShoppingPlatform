package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.CartItem;

import java.util.List;

@Mapper
public interface CartItemMapper {
    @Select("SELECT * FROM cart_items WHERE cart_id=#{cartId}")
    List<CartItem> listByCartId(@Param("cartId") Long cartId);

    @Select("SELECT * FROM cart_items WHERE cart_id=#{cartId} AND product_id=#{productId}")
    CartItem findOne(@Param("cartId") Long cartId, @Param("productId") Long productId);

    @Insert("INSERT INTO cart_items(cart_id,product_id,qty) VALUES(#{cartId},#{productId},#{qty})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CartItem it);

    @Update("UPDATE cart_items SET qty=#{qty} WHERE id=#{id}")
    int updateQty(@Param("id") Long id, @Param("qty") Integer qty);

    @Delete("DELETE FROM cart_items WHERE cart_id=#{cartId} AND product_id=#{productId}")
    int deleteOne(@Param("cartId") Long cartId, @Param("productId") Long productId);

    @Delete("DELETE FROM cart_items WHERE cart_id=#{cartId}")
    int clear(@Param("cartId") Long cartId);
}
