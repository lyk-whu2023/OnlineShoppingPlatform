package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id=#{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM users WHERE username=#{username}")
    User findByUsername(@Param("username") String username);
    
    @Select("SELECT * FROM users WHERE phone=#{phone}")
    User findByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Insert("INSERT INTO users(username,password_hash,role,phone) VALUES(#{username},#{passwordHash},#{role},#{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE users SET phone=#{phone}, status=#{status}, role=#{role} WHERE id=#{id}")
    int update(User user);

    @Update("UPDATE users SET password_hash=#{passwordHash} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);

    @Update("UPDATE users SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM users WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
