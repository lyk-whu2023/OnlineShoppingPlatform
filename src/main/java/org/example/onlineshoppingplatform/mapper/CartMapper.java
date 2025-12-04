package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Cart;

@Mapper
public interface CartMapper {
    @Select("SELECT * FROM carts WHERE user_id=#{userId}")
    Cart findByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO carts(user_id) VALUES(#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Cart c);

    @Delete("DELETE FROM carts WHERE user_id=#{userId}")
    int deleteByUserId(@Param("userId") Long userId);
}
