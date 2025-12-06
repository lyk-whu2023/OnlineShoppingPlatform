package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Order;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders WHERE id=#{id}")
    Order findById(@Param("id") Long id);

    @Select("SELECT * FROM orders WHERE user_id=#{userId} ORDER BY created_at DESC")
    List<Order> listByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM orders WHERE status=#{status} ORDER BY created_at DESC")
    List<Order> listByStatus(@Param("status") String status);

    @Select("SELECT * FROM orders ORDER BY created_at DESC")
    List<Order> listAll();

    @Insert("INSERT INTO orders(user_id,address_id,amount,status,consignee_name,consignee_phone,consignee_detail) VALUES(#{userId},#{addressId},#{amount},#{status},#{consigneeName},#{consigneePhone},#{consigneeDetail})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order o);

    @Update("UPDATE orders SET status=#{status}, amount=#{amount}, address_id=#{addressId}, consignee_name=#{consigneeName}, consignee_phone=#{consigneePhone}, consignee_detail=#{consigneeDetail} WHERE id=#{id}")
    int update(Order o);

    @Update("UPDATE orders SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM orders WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
