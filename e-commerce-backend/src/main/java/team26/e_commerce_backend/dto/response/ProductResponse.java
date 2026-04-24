package team26.e_commerce_backend.dto.response;

import java.math.BigDecimal;
import team26.e_commerce_backend.entity.Product;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stockQty,
    Long sellerId,
    String sellerName,
    String categoryName) {
  public static ProductResponse fromEntity(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getStockQty(),
        product.getSeller().getUserId(),
        product.getSeller().getBusinessName(),
        product.getCategory().getName());
  }
}
