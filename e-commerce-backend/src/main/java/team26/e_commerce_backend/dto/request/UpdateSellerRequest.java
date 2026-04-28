package team26.e_commerce_backend.dto.request;

import jakarta.validation.constraints.Email;

public record UpdateSellerRequest(
    String businessName, String description, @Email String contactEmail) {}
