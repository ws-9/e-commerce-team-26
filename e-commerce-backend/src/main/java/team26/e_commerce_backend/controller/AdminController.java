package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team26.e_commerce_backend.dto.response.AdminResponse;
import team26.e_commerce_backend.service.AdminService;

@Tag(name = "Admin controller")
@RestController
@RequestMapping("/api/admins")
public class AdminController {
  private final AdminService adminService;

  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @GetMapping
  @Operation(summary = "Get all admins")
  public List<AdminResponse> getAllAdmins() {
    return adminService.getAllAdmins();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get admin by id")
  public AdminResponse getAdmin(@PathVariable long id) {
    return adminService.getAdmin(id);
  }
}
