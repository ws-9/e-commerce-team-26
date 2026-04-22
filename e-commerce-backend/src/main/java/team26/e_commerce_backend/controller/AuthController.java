package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team26.e_commerce_backend.dto.request.RegisterRequest;
import team26.e_commerce_backend.service.AuthService;

@Tag(name = "Authentication Controller")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "Create a new user account")
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request)
      throws Exception {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
