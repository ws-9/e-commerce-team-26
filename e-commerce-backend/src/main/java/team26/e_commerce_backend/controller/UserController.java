package team26.e_commerce_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import team26.e_commerce_backend.dto.request.UpdateUserRequest;
import team26.e_commerce_backend.dto.response.UserResponse;
import team26.e_commerce_backend.service.UserService;

@Tag(name = "User controller")
@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "Get all users")
  public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get user by id")
  public UserResponse getUser(@PathVariable long id) {
    return userService.getUser(id);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
      summary = "Delete my account",
      description = "Deletes the authenticated user's profile.")
  public void deleteMe() {
    userService.deleteMe();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
      summary = "Admin: Delete user by ID",
      description = "Deletes a specific user profile by their ID. Requires admin privileges.")
  public void deleteUserById(@PathVariable long id) {
    userService.deleteUserById(id);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update user details")
  public UserResponse updateUser(@PathVariable long id, @RequestBody UpdateUserRequest request) {
    return userService.updateUser(id, request);
  }
}
