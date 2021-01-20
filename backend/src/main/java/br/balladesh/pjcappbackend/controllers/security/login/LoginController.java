package br.balladesh.pjcappbackend.controllers.security.login;

import br.balladesh.pjcappbackend.config.security.jwt.JwtUtilities;
import br.balladesh.pjcappbackend.config.security.services.MyUserDetails;
import br.balladesh.pjcappbackend.dto.security.JwtJsonResponse;
import br.balladesh.pjcappbackend.dto.security.UserLoginRequest;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class LoginController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtilities jwtUtils;
  private final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @Autowired
  public LoginController(AuthenticationManager authenticationManager, JwtUtilities jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/login")
  public ResponseEntity<?> authUser(@RequestBody UserLoginRequest loginRequest) {
    if( NonNull.withParams(this.authenticationManager, this.jwtUtils).check() ) {
      this.logger.error("LoginController::authUser Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      Authentication authentication = this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(),
              loginRequest.getPassword()
          )
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = this.jwtUtils.generateJwtToken(authentication);

      MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

      return ResponseEntity.ok(new JwtJsonResponse(userDetails.getUsername(), jwt));
    } catch(Exception ignore) {
      return ResponseCreator.create("Your credentials is incorrect!", HttpStatus.UNAUTHORIZED);
    }
  }
}
