package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team26.e_commerce_backend.dto.request.CreateProductRequest;
import team26.e_commerce_backend.dto.request.UpdateProductRequest;
import team26.e_commerce_backend.dto.response.ProductResponse;
import team26.e_commerce_backend.service.ProductService;

@Tag(name = "Product Controller")
@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "Get all products")
  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAllProducts(
      @RequestParam(required = false, name = "seller_id") Long sellerUserId) {
    return ResponseEntity.ok(productService.getAllProducts(sellerUserId));
  }

  @Operation(summary = "Get product by ID")
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @Operation(
      summary = "Create product",
      description = "Creates a new product. Only users with a seller profile can perform this action.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ProductResponse> createProduct(
      @RequestBody @Valid CreateProductRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
  }

  @Operation(
      summary = "Update product",
      description = "Updates a product by its ID. Only the seller who owns the product or an admin can perform this action.")
  @PatchMapping("/{id}")
  public ResponseEntity<ProductResponse> updateProduct(
      @PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
    return ResponseEntity.ok(productService.updateProduct(id, request));
  }

  @Operation(
      summary = "Delete product",
      description =
          "Deletes a product by its ID. Only the seller who owns the product or an admin can perform this action.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
  }
}
