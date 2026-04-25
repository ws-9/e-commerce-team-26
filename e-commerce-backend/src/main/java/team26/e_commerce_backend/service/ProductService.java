package team26.e_commerce_backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team26.e_commerce_backend.dao.ProductRepository;
import team26.e_commerce_backend.dto.response.ProductResponse;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream().map(ProductResponse::fromEntity).toList();
  }

  @Transactional(readOnly = true)
  public ProductResponse getProductById(Long id) {
    return productRepository
        .findById(id)
        .map(ProductResponse::fromEntity)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
  }
}
