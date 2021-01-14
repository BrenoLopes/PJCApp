package br.balladesh.pjcappbackend.controllers.security.refresh;

import br.balladesh.pjcappbackend.config.security.jwt.JwtUtilities;
import br.balladesh.pjcappbackend.utilities.builders.BuildResponseFromException;
import br.balladesh.pjcappbackend.dto.security.JsonResponse;

import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class RefreshController {
  private final JwtUtilities jwtUtils;

  private static final Logger logger = LoggerFactory.getLogger(RefreshController.class);

  @Autowired
  public RefreshController(JwtUtilities jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refreshJwtToken(HttpServletRequest request) {
    final Optional<Boolean> jwtIsAlreadyValid = Optional.ofNullable((Boolean) request.getAttribute("jwt_is_valid"));

    if(jwtIsAlreadyValid.isPresent()) {
      return this.showCurrentJwtToken(request);
    } else {
      return this.showNewRefreshedJwtToken(request);
    }
  }

  private ResponseEntity<?> showCurrentJwtToken(HttpServletRequest request) {
    final Optional<String> jwtToken = Optional.ofNullable(request.getAttribute("jwt_token").toString());
    Optional<String> username = Optional.ofNullable(request.getAttribute("username").toString());

    if (!jwtToken.isPresent() || !username.isPresent()) {
      logger.error("RefreshController::showCurrentJwtToken The token should be valid and be reused, but it's null.");
      return this.showInternalServerError();
    }

    return ResponseEntity.ok(new JsonResponse(username.get(), jwtToken.get()));
  }

  private ResponseEntity<?> showNewRefreshedJwtToken(HttpServletRequest request) {
    final DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");

    // Get the username from the claims that was injected during the auth token filter if the token is invalid
    Optional<String> username = Optional.ofNullable(claims.get("sub").toString());

    if(!username.isPresent()) {
      logger.error("RefreshController::showNewRefreshedJwtToken The token should be valid and be reused, but it's null.");
      return this.showInternalServerError();
    }

    final String token = this.jwtUtils.generateRefreshToken(username.get());
    return ResponseEntity.ok(new JsonResponse(username.get(), token));
  }

  private ResponseEntity<?> showInternalServerError() {
    return new BuildResponseFromException(new InternalServerErrorException("An error happened inside the server"))
        .build().getData();
  }
}
