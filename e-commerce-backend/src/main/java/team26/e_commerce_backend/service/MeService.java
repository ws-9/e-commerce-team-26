package team26.e_commerce_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.component.AuthUtilsComponent;
import team26.e_commerce_backend.dao.AdminRepository;
import team26.e_commerce_backend.dao.SellerRepository;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.response.MeResponse;
import team26.e_commerce_backend.entity.User;

@Service
public class MeService {
  private final UserRepository userRepository;
  private final AdminRepository adminRepository;
  private final SellerRepository sellerRepository;
  private final AuthUtilsComponent authUtilsComponent;

  public MeService(
      UserRepository userRepository,
      AdminRepository adminRepository,
      SellerRepository sellerRepository,
      AuthUtilsComponent authUtilsComponent) {
    this.userRepository = userRepository;
    this.adminRepository = adminRepository;
    this.sellerRepository = sellerRepository;
    this.authUtilsComponent = authUtilsComponent;
  }

  @Transactional(readOnly = true)
  public MeResponse getMe() {
    long userId = authUtilsComponent.getAuthenticatedUserId();

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    boolean isAdmin = adminRepository.existsById(userId);
    boolean isSeller = sellerRepository.existsById(userId);

    return new MeResponse(
        user.getId(), user.getEmail(), user.getName(), user.getAddress(), isAdmin, isSeller);
  }
}
