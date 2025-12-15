package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Favorite;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    @Select("SELECT * FROM favorites WHERE user_id=#{userId} ORDER BY created_at DESC")
    List<Favorite> listByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO favorites(user_id,product_id,created_at) VALUES(#{userId},#{productId},COALESCE(#{createdAt},NOW()))")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Favorite f);

    @Delete("DELETE FROM favorites WHERE user_id=#{userId} AND product_id=#{productId}")
    int delete(@Param("userId") Long userId, @Param("productId") Long productId);

    @Select("SELECT COUNT(1) FROM favorites WHERE user_id=#{userId} AND product_id=#{productId}")
    int exists(@Param("userId") Long userId, @Param("productId") Long productId);
}
