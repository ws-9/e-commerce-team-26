package team26.e_commerce_backend.service;

import org.springframework.stereotype.Service;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.request.RegisterRequest;
import team26.e_commerce_backend.entity.User;

@Service
public class AuthService {
  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void register(RegisterRequest request) throws Exception {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new Exception("Email is already taken");
    }
    userRepository.save(
        new User(request.email(), request.password(), request.name(), request.address()));
  }
}
