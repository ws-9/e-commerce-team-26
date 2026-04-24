package team26.e_commerce_backend.dto.response;

public record StockErrorResponse(
    Long productId, String productName, Integer requestedQuantity, Integer availableQuantity) {}
