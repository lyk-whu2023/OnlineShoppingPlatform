package org.example.onlineshoppingplatform.mapper;

import org.apache.ibatis.annotations.*;
import org.example.onlineshoppingplatform.entity.Shipment;

@Mapper
public interface ShipmentMapper {
    @Select("SELECT * FROM shipments WHERE order_id=#{orderId}")
    Shipment findByOrderId(@Param("orderId") Long orderId);

    @Insert("INSERT INTO shipments(order_id,carrier,tracking_no,status,shipped_at,delivered_at) VALUES(#{orderId},#{carrier},#{trackingNo},#{status},#{shippedAt},#{deliveredAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Shipment s);

    @Update("UPDATE shipments SET carrier=#{carrier}, tracking_no=#{trackingNo}, status=#{status}, shipped_at=#{shippedAt}, delivered_at=#{deliveredAt} WHERE id=#{id}")
    int update(Shipment s);

    @Update("UPDATE shipments SET status=#{status}, shipped_at=#{shippedAt}, delivered_at=#{deliveredAt} WHERE order_id=#{orderId}")
    int updateStatus(@Param("orderId") Long orderId,
                     @Param("status") String status,
                     @Param("shippedAt") java.time.LocalDateTime shippedAt,
                     @Param("deliveredAt") java.time.LocalDateTime deliveredAt);
}
