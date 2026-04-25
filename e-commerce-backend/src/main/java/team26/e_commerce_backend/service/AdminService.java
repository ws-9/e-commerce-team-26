package team26.e_commerce_backend.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.dao.AdminRepository;
import team26.e_commerce_backend.dto.response.AdminResponse;

@Service
public class AdminService {
  private final AdminRepository adminRepository;

  public AdminService(AdminRepository adminRepository) {
    this.adminRepository = adminRepository;
  }

  @Transactional(readOnly = true)
  public List<AdminResponse> getAllAdmins() {
    return adminRepository.findAll().stream().map(AdminResponse::fromEntity).toList();
  }

  @Transactional(readOnly = true)
  public AdminResponse getAdmin(long id) {
    return adminRepository
        .findById(id)
        .map(AdminResponse::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
