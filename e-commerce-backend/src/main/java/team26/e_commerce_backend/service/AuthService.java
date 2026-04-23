package team26.e_commerce_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team26.e_commerce_backend.dao.SellerRepository;
import team26.e_commerce_backend.dao.UserRepository;
import team26.e_commerce_backend.dto.request.LoginRequest;
import team26.e_commerce_backend.dto.request.RegisterRequest;
import team26.e_commerce_backend.dto.request.SellerRegisterRequest;
import team26.e_commerce_backend.dto.response.AuthResponse;
import team26.e_commerce_backend.entity.Seller;
import team26.e_commerce_backend.entity.User;
import team26.e_commerce_backend.entity.UserRole;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final SellerRepository sellerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      UserRepository userRepository,
      SellerRepository sellerRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService) {
    this.userRepository = userRepository;
    this.sellerRepository = sellerRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponse register(RegisterRequest request) throws Exception {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new Exception("Email is already taken");
    }
    User user =
        new User(
            request.email(),
            passwordEncoder.encode(request.password()),
            request.name(),
            request.address(),
            UserRole.SHOPPER);
    userRepository.save(user);
    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
    return new AuthResponse(token, user.getRole().name(), user.getEmail(), user.getName());
  }

  @Transactional
  public AuthResponse registerSeller(SellerRegisterRequest request) throws Exception {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new Exception("Email is already taken");
    }
    User user =
        new User(
            request.email(),
            passwordEncoder.encode(request.password()),
            request.name(),
            request.address(),
            UserRole.SELLER);
    userRepository.save(user);
    Seller seller =
        new Seller(user, request.businessName(), request.description(), request.taxId(), request.contactEmail());
    sellerRepository.save(seller);
    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
    return new AuthResponse(token, user.getRole().name(), user.getEmail(), user.getName());
  }

  public AuthResponse login(LoginRequest request) throws Exception {
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new Exception("Invalid email or password"));
    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new Exception("Invalid email or password");
    }
    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
    return new AuthResponse(token, user.getRole().name(), user.getEmail(), user.getName());
  }
}
