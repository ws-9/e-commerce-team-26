package team26.e_commerce_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.request.RegisterRequest;
import team26.e_commerce_backend.entity.User;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void register(RegisterRequest request) throws Exception {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new Exception("Email is already taken");
    }
    userRepository.save(
        new User(
            request.email(),
            passwordEncoder.encode(request.password()),
            request.name(),
            request.address()));
  }
}
