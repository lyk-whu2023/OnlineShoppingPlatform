package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Address;

import java.util.List;

@Mapper
public interface AddressMapper {
    @Select("SELECT * FROM addresses WHERE id=#{id}")
    Address findById(@Param("id") Long id);

    @Select("SELECT * FROM addresses WHERE user_id=#{userId}")
    List<Address> listByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO addresses(user_id,name,phone,detail,is_default) VALUES(#{userId},#{name},#{phone},#{detail},#{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Address a);

    @Update("UPDATE addresses SET name=#{name}, phone=#{phone}, detail=#{detail}, is_default=#{isDefault} WHERE id=#{id}")
    int update(Address a);

    @Update("UPDATE addresses SET is_default=false WHERE user_id=#{userId}")
    int unsetDefaultByUserId(@Param("userId") Long userId);

    @Delete("DELETE FROM addresses WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
