package team26.e_commerce_backend.dto.response;

import team26.e_commerce_backend.entity.User;

public record UserResponse(long id, String email, String name, String address) {
  public static UserResponse fromEntity(User user) {
    return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getAddress());
  }
}
