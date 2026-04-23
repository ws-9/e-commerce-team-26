package team26.e_commerce_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.dao.AdminRepository;
import team26.e_commerce_backend.dto.response.AdminResponse;

@Service
public class AdminService {
  private final AdminRepository adminRepository;

  public AdminService(AdminRepository adminRepository) {
    this.adminRepository = adminRepository;
  }

  public AdminResponse getAdmin(long id) {
    return adminRepository
        .findById(id)
        .map(AdminResponse::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
