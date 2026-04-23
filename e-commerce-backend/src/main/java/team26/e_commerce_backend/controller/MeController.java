package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team26.e_commerce_backend.dto.response.MeResponse;
import team26.e_commerce_backend.service.MeService;

@Tag(name = "Me controller")
@RestController
@RequestMapping("/api/me")
public class MeController {
  private final MeService meService;

  public MeController(MeService meService) {
    this.meService = meService;
  }

  @GetMapping
  @Operation(summary = "Get authenticated user info")
  public MeResponse getMe() {
    return meService.getMe();
  }
}
