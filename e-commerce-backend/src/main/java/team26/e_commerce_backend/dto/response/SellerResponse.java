package team26.e_commerce_backend.dto.response;

import team26.e_commerce_backend.entity.Seller;

public record SellerResponse(
    long userId,
    String businessName,
    String description,
    boolean verified,
    String taxId,
    String contactEmail) {
  public static SellerResponse fromEntity(Seller seller) {
    return new SellerResponse(
        seller.getUserId(),
        seller.getBusinessName(),
        seller.getDescription(),
        seller.getVerified(),
        seller.getTaxId(),
        seller.getContactEmail());
  }
}
