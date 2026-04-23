package team26.e_commerce_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.response.UserResponse;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserResponse getUser(long id) {
    return userRepository
        .findById(id)
        .map(UserResponse::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
