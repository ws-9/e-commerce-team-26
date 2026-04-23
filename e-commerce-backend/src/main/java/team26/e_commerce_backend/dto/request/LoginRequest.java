package team26.e_commerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull @NotBlank String email, @NotNull @NotBlank String password) {}
