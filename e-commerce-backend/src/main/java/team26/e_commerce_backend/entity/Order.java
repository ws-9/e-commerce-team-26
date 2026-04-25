package team26.e_commerce_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "date", nullable = false)
  private Instant date = Instant.now();

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "total", nullable = false)
  private BigDecimal total;

  @Column(name = "shipping_address", nullable = false)
  private String shippingAddress;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  public Order() {}

  public Order(User user, String status, BigDecimal total, String shippingAddress) {
    this.user = user;
    this.status = status;
    this.total = total;
    this.shippingAddress = shippingAddress;
    this.date = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public User getUser() {
    return user;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }
}
