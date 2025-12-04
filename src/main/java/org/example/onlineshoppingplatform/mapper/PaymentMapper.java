package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Payment;

@Mapper
public interface PaymentMapper {
    @Select("SELECT * FROM payments WHERE order_id=#{orderId}")
    Payment findByOrderId(@Param("orderId") Long orderId);

    @Insert("INSERT INTO payments(order_id,amount,method,status,paid_at) VALUES(#{orderId},#{amount},#{method},#{status},#{paidAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Payment p);

    @Update("UPDATE payments SET amount=#{amount}, method=#{method}, status=#{status}, paid_at=#{paidAt} WHERE id=#{id}")
    int update(Payment p);

    @Update("UPDATE payments SET status=#{status}, paid_at=#{paidAt} WHERE order_id=#{orderId}")
    int updateStatus(@Param("orderId") Long orderId,
                     @Param("status") String status,
                     @Param("paidAt") java.time.LocalDateTime paidAt);
}
