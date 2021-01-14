package br.balladesh.pjcappbackend.controllers.security.refresh;

import br.balladesh.pjcappbackend.config.security.jwt.JwtUtilities;
import br.balladesh.pjcappbackend.dto.security.JsonResponse;
import br.balladesh.pjcappbackend.repository.security.UserRepository;

import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class RefreshController {
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtilities jwtUtils;

  @Autowired
  public RefreshController(
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtUtilities jwtUtils
  ) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtils = jwtUtils;
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refreshJwtToken(HttpServletRequest request) {
    final DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
    final boolean jwtIsAlreadyValid = (boolean) request.getAttribute("jwt_is_valid");
    final String jwtToken = (String) request.getAttribute("jwt_token");
    String username = (String) request.getAttribute("username");

    if(jwtIsAlreadyValid) {
      return ResponseEntity.ok(new JsonResponse(username, jwtToken));
    }

    // Get the username from the claims that was injected during the auth token filter if the token is invalid
    username = claims.get("sub").toString();

    final String token = this.jwtUtils.generateRefreshToken(username);
    return ResponseEntity.ok(new JsonResponse(username, token));
  }
}
