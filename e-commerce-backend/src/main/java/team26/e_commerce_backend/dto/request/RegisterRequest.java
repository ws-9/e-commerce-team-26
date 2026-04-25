package team26.e_commerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotNull String email,
    @NotNull
        @NotBlank(message = "Password cannot be whitespace")
        @Size(min = 8, max = 50, message = "Password must be between {min} and {max} characters")
        String password,
    @NotNull String name,
    @NotNull String address) {}
