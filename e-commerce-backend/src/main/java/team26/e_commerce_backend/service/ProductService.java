package team26.e_commerce_backend.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.component.AuthUtilsComponent;
import team26.e_commerce_backend.dao.AdminRepository;
import team26.e_commerce_backend.dao.CategoryRepository;
import team26.e_commerce_backend.dao.ProductRepository;
import team26.e_commerce_backend.dao.SellerRepository;
import team26.e_commerce_backend.dto.request.CreateProductRequest;
import team26.e_commerce_backend.dto.request.UpdateProductRequest;
import team26.e_commerce_backend.dto.response.ProductResponse;
import team26.e_commerce_backend.entity.Category;
import team26.e_commerce_backend.entity.Product;
import team26.e_commerce_backend.entity.Seller;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final SellerRepository sellerRepository;
  private final CategoryRepository categoryRepository;
  private final AdminRepository adminRepository;
  private final AuthUtilsComponent authUtils;

  public ProductService(
      ProductRepository productRepository,
      SellerRepository sellerRepository,
      CategoryRepository categoryRepository,
      AdminRepository adminRepository,
      AuthUtilsComponent authUtils) {
    this.productRepository = productRepository;
    this.sellerRepository = sellerRepository;
    this.categoryRepository = categoryRepository;
    this.adminRepository = adminRepository;
    this.authUtils = authUtils;
  }

  @Transactional(readOnly = true)
  public List<ProductResponse> getAllProducts(Long sellerUserId) {
    if (sellerUserId != null) {
      return productRepository.findBySeller_userId(sellerUserId).stream()
          .map(ProductResponse::fromEntity)
          .toList();
    }
    return productRepository.findAll().stream().map(ProductResponse::fromEntity).toList();
  }

  @Transactional(readOnly = true)
  public ProductResponse getProductById(Long id) {
    return productRepository
        .findById(id)
        .map(ProductResponse::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
  }

  @Transactional
  public ProductResponse createProduct(CreateProductRequest request) {
    Long authenticatedId = authUtils.getAuthenticatedUserId();
    Seller seller =
        sellerRepository
            .findById(authenticatedId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Only sellers can create products"));

    // Hardcode category to 'Default Category' (ID 1) as it's the only one for now
    Category category =
        categoryRepository
            .findById(1L)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Default category not found"));

    Product product = new Product();
    product.setName(request.name());
    product.setDescription(request.description());
    product.setPrice(request.price());
    product.setStockQty(request.stockQty());
    product.setSeller(seller);
    product.setCategory(category);

    return ProductResponse.fromEntity(productRepository.save(product));
  }

  @Transactional
  public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
    Long authenticatedId = authUtils.getAuthenticatedUserId();
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    boolean isAdmin = adminRepository.existsById(authenticatedId);
    boolean isOwner = product.getSeller().getUser().getId().equals(authenticatedId);

    if (!isOwner && !isAdmin) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Only the seller or an admin can update this product");
    }

    if (request.name() != null) {
      product.setName(request.name());
    }
    if (request.description() != null) {
      product.setDescription(request.description());
    }
    if (request.price() != null) {
      product.setPrice(request.price());
    }
    if (request.stockQty() != null) {
      product.setStockQty(request.stockQty());
    }

    return ProductResponse.fromEntity(productRepository.save(product));
  }

  @Transactional
  public void deleteProduct(Long id) {
    Long authenticatedId = authUtils.getAuthenticatedUserId();
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    boolean isAdmin = adminRepository.existsById(authenticatedId);
    boolean isOwner = product.getSeller().getUser().getId().equals(authenticatedId);

    if (!isOwner && !isAdmin) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Only the seller or an admin can delete this product");
    }

    productRepository.delete(product);
  }
}
