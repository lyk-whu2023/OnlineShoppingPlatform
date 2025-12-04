package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.OrderItem;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    @Select("SELECT * FROM order_items WHERE order_id=#{orderId}")
    List<OrderItem> listByOrderId(@Param("orderId") Long orderId);

    @Insert("INSERT INTO order_items(order_id,product_id,name,price,qty) VALUES(#{orderId},#{productId},#{name},#{price},#{qty})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem it);

    @Delete("DELETE FROM order_items WHERE order_id=#{orderId}")
    int deleteByOrderId(@Param("orderId") Long orderId);
}
