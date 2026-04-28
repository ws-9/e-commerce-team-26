package team26.e_commerce_backend.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.component.AuthUtilsComponent;
import team26.e_commerce_backend.dao.SellerRepository;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.request.CreateSellerRequest;
import team26.e_commerce_backend.dto.request.UpdateSellerRequest;
import team26.e_commerce_backend.dto.response.SellerResponse;
import team26.e_commerce_backend.entity.Seller;
import team26.e_commerce_backend.entity.User;

@Service
public class SellerService {
  private final SellerRepository sellerRepository;
  private final UserRepository userRepository;
  private final AuthUtilsComponent authUtils;

  public SellerService(
      SellerRepository sellerRepository,
      UserRepository userRepository,
      AuthUtilsComponent authUtils) {
    this.sellerRepository = sellerRepository;
    this.userRepository = userRepository;
    this.authUtils = authUtils;
  }

  @Transactional(readOnly = true)
  public List<SellerResponse> getAllSellers() {
    return sellerRepository.findAll().stream().map(SellerResponse::fromEntity).toList();
  }

  @Transactional(readOnly = true)
  public SellerResponse getSeller(long id) {
    return sellerRepository
        .findById(id)
        .map(SellerResponse::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @Transactional
  public void becomeSeller(CreateSellerRequest request) {
    Long userId = authUtils.getAuthenticatedUserId();

    if (sellerRepository.existsById(userId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has a business");
    }

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Seller seller =
        new Seller(
            user,
            request.businessName(),
            request.description(),
            request.taxId(),
            request.contactEmail());

    sellerRepository.save(seller);
  }

  @Transactional
  public void updateSeller(UpdateSellerRequest request) {
    Long userId = authUtils.getAuthenticatedUserId();
    Seller seller =
        sellerRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Authenticated user is not a seller"));

    if (request.businessName() != null) {
      seller.setBusinessName(request.businessName());
    }
    if (request.description() != null) {
      seller.setDescription(request.description());
    }
    if (request.contactEmail() != null) {
      seller.setContactEmail(request.contactEmail());
    }
  }
}
