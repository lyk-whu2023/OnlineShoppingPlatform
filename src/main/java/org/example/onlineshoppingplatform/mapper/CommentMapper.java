package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("SELECT * FROM comments WHERE id=#{id}")
    Comment findById(@Param("id") Long id);

    @Select("SELECT * FROM comments WHERE product_id=#{productId} AND (parent_id IS NULL OR parent_id=0) AND status='visible' ORDER BY created_at DESC")
    List<Comment> listTopByProductId(@Param("productId") Long productId);

    @Select("SELECT * FROM comments WHERE parent_id=#{parentId} AND status='visible' ORDER BY created_at ASC")
    List<Comment> listReplies(@Param("parentId") Long parentId);

    @Select("SELECT * FROM comments ORDER BY created_at DESC")
    List<Comment> listAll();

    @Insert("INSERT INTO comments(product_id,user_id,content,parent_id,status,created_at) VALUES(#{productId},#{userId},#{content},#{parentId},COALESCE(#{status},'visible'),COALESCE(#{createdAt},NOW()))")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment c);

    @Update("UPDATE comments SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM comments WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
