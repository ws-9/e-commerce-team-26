package team26.e_commerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SellerRegisterRequest(
    @NotNull @NotBlank String email,
    @NotNull
        @NotBlank(message = "Password cannot be whitespace")
        @Size(min = 8, max = 50, message = "Password must be between {min} and {max} characters")
        String password,
    @NotNull @NotBlank String name,
    @NotNull @NotBlank String address,
    @NotNull @NotBlank String businessName,
    String description,
    @NotNull @NotBlank String taxId,
    @NotNull @NotBlank String contactEmail) {}
