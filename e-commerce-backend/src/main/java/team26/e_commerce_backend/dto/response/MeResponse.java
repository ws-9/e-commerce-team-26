package team26.e_commerce_backend.dto.response;

public record MeResponse(
    long id, String email, String name, String address, boolean isAdmin, boolean isSeller) {}
