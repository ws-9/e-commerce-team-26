package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team26.e_commerce_backend.dto.request.PlaceOrderRequest;
import team26.e_commerce_backend.service.OrderService;

@Tag(name = "Order Controller")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Operation(summary = "Place a new order")
  @PostMapping
  public ResponseEntity<Void> placeOrder(@RequestBody @Valid PlaceOrderRequest request) {
    orderService.placeOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
