package team26.e_commerce_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "qty", nullable = false)
  private Integer qty;

  @Column(name = "price_at_purchase", nullable = false)
  private BigDecimal priceAtPurchase;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  public OrderItem() {}

  public OrderItem(Order order, Product product, Integer qty) {
    this.order = order;
    this.product = product;
    this.qty = qty;
    this.priceAtPurchase = product.getPrice();
  }

  public Long getId() {
    return id;
  }

  public Integer getQty() {
    return qty;
  }

  public BigDecimal getPriceAtPurchase() {
    return priceAtPurchase;
  }

  public Order getOrder() {
    return order;
  }

  public Product getProduct() {
    return product;
  }
}
