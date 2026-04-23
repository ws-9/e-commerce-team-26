package team26.e_commerce_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.dao.SellerRepository;
import team26.e_commerce_backend.dto.response.SellerResponse;

@Service
public class SellerService {
  private final SellerRepository sellerRepository;

  public SellerService(SellerRepository sellerRepository) {
    this.sellerRepository = sellerRepository;
  }

  public SellerResponse getSeller(long id) {
    return sellerRepository
        .findById(id)
        .map(SellerResponse::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
