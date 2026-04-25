package team26.e_commerce_backend.dto.response;

import java.math.BigDecimal;
import team26.e_commerce_backend.entity.OrderItem;

public record OrderItemResponse(
    Long id, Long productId, String productName, Integer quantity, BigDecimal priceAtPurchase) {
  public static OrderItemResponse fromEntity(OrderItem item) {
    return new OrderItemResponse(
        item.getId(),
        item.getProduct() != null ? item.getProduct().getId() : null,
        item.getProduct() != null ? item.getProduct().getName() : "Deleted Product",
        item.getQty(),
        item.getPriceAtPurchase());
  }
}
