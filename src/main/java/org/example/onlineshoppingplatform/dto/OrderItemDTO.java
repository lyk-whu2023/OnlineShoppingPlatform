package org.example.onlineshoppingplatform.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer qty;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}
