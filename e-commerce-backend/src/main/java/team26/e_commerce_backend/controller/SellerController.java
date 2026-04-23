package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team26.e_commerce_backend.dto.response.SellerResponse;
import team26.e_commerce_backend.service.SellerService;

@Tag(name = "Seller controller")
@RestController
@RequestMapping("/api/sellers")
public class SellerController {
  private final SellerService sellerService;

  public SellerController(SellerService sellerService) {
    this.sellerService = sellerService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get seller by id")
  public SellerResponse getSeller(@PathVariable long id) {
    return sellerService.getSeller(id);
  }
}
