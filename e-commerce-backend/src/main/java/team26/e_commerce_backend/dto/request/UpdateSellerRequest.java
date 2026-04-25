package team26.e_commerce_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateSellerRequest(
    @NotBlank String businessName, String description, @NotBlank @Email String contactEmail) {}
