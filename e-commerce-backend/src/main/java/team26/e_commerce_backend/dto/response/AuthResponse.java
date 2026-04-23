package team26.e_commerce_backend.dto.response;

public record AuthResponse(String token, String role, String email, String name) {}
