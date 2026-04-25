package team26.e_commerce_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import team26.e_commerce_backend.entity.Order;

public record OrderResponse(
    Long id,
    Instant date,
    String status,
    BigDecimal total,
    String shippingAddress,
    List<OrderItemResponse> items) {
  public static OrderResponse fromEntity(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getDate(),
        order.getStatus(),
        order.getTotal(),
        order.getShippingAddress(),
        order.getOrderItems().stream().map(OrderItemResponse::fromEntity).toList());
  }
}
