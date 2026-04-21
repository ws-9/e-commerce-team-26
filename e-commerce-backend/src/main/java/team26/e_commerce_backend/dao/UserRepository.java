package team26.e_commerce_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import team26.e_commerce_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {}
