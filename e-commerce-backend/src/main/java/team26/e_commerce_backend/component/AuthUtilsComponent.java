package team26.e_commerce_backend.component;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import team26.e_commerce_backend.dto.auth.SecurityUser;

@Component
public class AuthUtilsComponent {
  public Long getAuthenticatedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();

    if (principal instanceof SecurityUser user) {
      return user.getUser().getId();
    }

    throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get authenticated user ID");
  }
}
