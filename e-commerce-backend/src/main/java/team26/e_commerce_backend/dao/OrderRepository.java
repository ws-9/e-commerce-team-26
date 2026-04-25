package team26.e_commerce_backend.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team26.e_commerce_backend.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserIdOrderByDateDesc(long userId);
}
