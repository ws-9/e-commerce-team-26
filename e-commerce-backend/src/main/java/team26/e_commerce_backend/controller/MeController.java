package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team26.e_commerce_backend.dto.response.MeResponse;
import team26.e_commerce_backend.dto.response.OrderResponse;
import team26.e_commerce_backend.dto.response.ProductResponse;
import team26.e_commerce_backend.service.MeService;
import team26.e_commerce_backend.service.OrderService;

@Tag(name = "Me controller")
@RestController
@RequestMapping("/api/me")
public class MeController {
  private final MeService meService;
  private final OrderService orderService;

  public MeController(MeService meService, OrderService orderService) {
    this.meService = meService;
    this.orderService = orderService;
  }

  @GetMapping
  @Operation(summary = "Get authenticated user info")
  public MeResponse getMe() {
    return meService.getMe();
  }

  @GetMapping("/orders")
  @Operation(summary = "Get authenticated user's orders")
  public List<OrderResponse> getMyOrders() {
    return orderService.getMyOrders();
  }

  @GetMapping("/products")
  @Operation(summary = "Get authenticated seller's products")
  public List<ProductResponse> getMyProducts() {
    return meService.getMyProducts();
  }
}
