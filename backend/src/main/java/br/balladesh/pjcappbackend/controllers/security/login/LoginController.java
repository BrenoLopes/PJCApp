package br.balladesh.pjcappbackend.controllers.security.login;

import br.balladesh.pjcappbackend.config.security.jwt.JwtUtilities;
import br.balladesh.pjcappbackend.config.security.services.MyUserDetails;
import br.balladesh.pjcappbackend.controllers.BuildResponseFromException;
import br.balladesh.pjcappbackend.dto.security.JsonResponse;
import br.balladesh.pjcappbackend.dto.security.LoginRequest;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.errors.UnauthorizedCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public LoginController(AuthenticationManager authenticationManager,JwtUtilities jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/login")
  public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {
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
      return ResponseEntity.ok(new JsonResponse(userDetails.getUsername(), jwt));
    } catch(Exception e) {
      HttpException _e = new UnauthorizedCredentialsException(
          "The informed credentials is incorrect!"
      );

      return new BuildResponseFromException(_e).build().getData();
    }
  }
}
