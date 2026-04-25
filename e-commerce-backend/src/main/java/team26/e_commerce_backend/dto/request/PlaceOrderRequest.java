package team26.e_commerce_backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PlaceOrderRequest(@NotEmpty @Valid List<OrderItemRequest> items) {}
