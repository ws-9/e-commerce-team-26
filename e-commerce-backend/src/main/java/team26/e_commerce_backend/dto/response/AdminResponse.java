package team26.e_commerce_backend.dto.response;

import team26.e_commerce_backend.entity.Admin;

public record AdminResponse(long userId, boolean enabled) {
  public static AdminResponse fromEntity(Admin admin) {
    return new AdminResponse(admin.getUserId(), admin.getEnabled());
  }
}
