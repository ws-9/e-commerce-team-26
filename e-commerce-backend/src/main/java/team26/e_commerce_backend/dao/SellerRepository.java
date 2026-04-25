package team26.e_commerce_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import team26.e_commerce_backend.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {}
