package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import team26.e_commerce_backend.dto.request.CreateSellerRequest;
import team26.e_commerce_backend.dto.request.UpdateSellerRequest;
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

  @GetMapping
  @Operation(summary = "Get all sellers")
  public List<SellerResponse> getAllSellers() {
    return sellerService.getAllSellers();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get seller by id")
  public SellerResponse getSeller(@PathVariable long id) {
    return sellerService.getSeller(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Become a seller")
  public void becomeSeller(@RequestBody @Valid CreateSellerRequest request) {
    sellerService.becomeSeller(request);
  }

  @PatchMapping
  @Operation(summary = "Update my business details")
  public void updateSeller(@RequestBody @Valid UpdateSellerRequest request) {
    sellerService.updateSeller(request);
  }
}
