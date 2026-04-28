package team26.e_commerce_backend.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.component.AuthUtilsComponent;
import team26.e_commerce_backend.dao.AdminRepository;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.request.UpdateUserRequest;
import team26.e_commerce_backend.dto.response.UserResponse;
import team26.e_commerce_backend.entity.User;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final AdminRepository adminRepository;
  private final AuthUtilsComponent authUtils;

  public UserService(
      UserRepository userRepository,
      AdminRepository adminRepository,
      AuthUtilsComponent authUtils) {
    this.userRepository = userRepository;
    this.adminRepository = adminRepository;
    this.authUtils = authUtils;
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream().map(UserResponse::fromEntity).toList();
  }

  @Transactional(readOnly = true)
  public UserResponse getUser(long id) {
    return userRepository
        .findById(id)
        .map(UserResponse::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @Transactional
  public void deleteUser(Long id) {
    Long authenticatedId = authUtils.getAuthenticatedUserId();
    boolean isAdmin = adminRepository.existsById(authenticatedId);

    long targetId = (id == null) ? authenticatedId : id;

    if (targetId != authenticatedId && !isAdmin) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Only admins can delete other accounts");
    }

    if (!userRepository.existsById(targetId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    userRepository.deleteById(targetId);
  }

  @Transactional
  public UserResponse updateUser(long id, UpdateUserRequest request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (request.name() != null) {
      user.setName(request.name());
    }
    if (request.address() != null) {
      user.setAddress(request.address());
    }

    return UserResponse.fromEntity(userRepository.save(user));
  }
}
