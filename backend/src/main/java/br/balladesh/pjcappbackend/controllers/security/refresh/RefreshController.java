package br.balladesh.pjcappbackend.controllers.security.refresh;

import br.balladesh.pjcappbackend.config.security.jwt.JwtUtilities;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.security.JwtJsonResponse;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import com.google.common.base.MoreObjects;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class RefreshController {
  private final JwtUtilities jwtUtils;
  private final UsersService usersService;

  private static final Logger logger = LoggerFactory.getLogger(RefreshController.class);

  @Autowired
  public RefreshController(JwtUtilities jwtUtils, UsersService usersService) {
    this.jwtUtils = jwtUtils;
    this.usersService = usersService;
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refreshJwtToken(HttpServletRequest request) {
    try {
      boolean isJwtStillValid = (boolean) MoreObjects.firstNonNull(request.getAttribute("jwt_is_valid"), false);

      if (!isJwtStillValid) {
        final DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");

        if (claims == null || claims.get("sub") == null)
          throw new NotFoundException();

        String user = claims.get("sub").toString();

        this.usersService.getUserBy(user).orElseThrow(NotFoundException::new);

        return this.showNewRefreshedJwtToken(request);
      }

      return this.showCurrentJwtToken(request);
    } catch (NotFoundException e) {
      return ResponseCreator.create(HttpStatus.NOT_FOUND);
    } catch(Exception e) {
      logger.error("RefreshController::refreshJwtToken failed to refresh jwt token. Error: {}", e.getMessage());
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<JwtJsonResponse> showCurrentJwtToken(HttpServletRequest request) throws NullPointerException {
    final String token = request.getAttribute("jwt_token").toString();
    String username = request.getAttribute("username").toString();

    return ResponseEntity.ok(new JwtJsonResponse(username, token));
  }

  private ResponseEntity<JwtJsonResponse> showNewRefreshedJwtToken(HttpServletRequest request) throws NullPointerException, ClassCastException {
    final DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");

    // Get the username from the claims that was injected during the auth token filter if the token is invalid
    String username = claims.get("sub").toString();

    final String token = this.jwtUtils.generateJwtToken(username);
    return ResponseEntity.ok(new JwtJsonResponse(username, token));
  }
}
