package team26.e_commerce_backend.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.dao.OrderRepository;
import team26.e_commerce_backend.dao.ProductRepository;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.request.OrderItemRequest;
import team26.e_commerce_backend.dto.request.PlaceOrderRequest;
import team26.e_commerce_backend.dto.response.StockErrorResponse;
import team26.e_commerce_backend.entity.Order;
import team26.e_commerce_backend.entity.OrderItem;
import team26.e_commerce_backend.entity.Product;
import team26.e_commerce_backend.entity.User;
import team26.e_commerce_backend.exception.InsufficientStockException;
import team26.e_commerce_backend.component.AuthUtilsComponent;

@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final AuthUtilsComponent authUtils;

  public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, AuthUtilsComponent authUtils) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.authUtils = authUtils;
  }

  @Transactional
  public void placeOrder(PlaceOrderRequest request) {
    Long userId = authUtils.getAuthenticatedUserId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    List<StockErrorResponse> stockErrors = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    // 1. Validate stock and existence
    for (OrderItemRequest itemReq : request.items()) {
      Product product = productRepository.findById(itemReq.productId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + itemReq.productId()));

      if (product.getStockQty() < itemReq.quantity()) {
        stockErrors.add(new StockErrorResponse(
            product.getId(),
            product.getName(),
            itemReq.quantity(),
            product.getStockQty()
        ));
      } else {
        total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));
      }
    }

    // 2. If any stock errors, throw exception
    if (!stockErrors.isEmpty()) {
      throw new InsufficientStockException(stockErrors);
    }

    // 3. Create Order
    Order order = new Order(user, "PLACED", total, user.getAddress());

    List<OrderItem> orderItems = new ArrayList<>();
    for (OrderItemRequest itemReq : request.items()) {
      Product product = productRepository.findById(itemReq.productId()).get();
      
      OrderItem orderItem = new OrderItem(order, product, itemReq.quantity());
      orderItems.add(orderItem);

      // Subtract stock
      product.setStockQty(product.getStockQty() - itemReq.quantity());
      productRepository.save(product);
    }

    order.setOrderItems(orderItems);
    orderRepository.save(order);
  }
}
