package br.balladesh.pjcappbackend.controllers.security.signup;

import br.balladesh.pjcappbackend.controllers.BuildResponseFromException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.security.SignUpRequest;
import br.balladesh.pjcappbackend.entity.security.UserEntity;
import br.balladesh.pjcappbackend.repository.security.UserRepository;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class SignUpController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

  @Autowired
  public SignUpController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpRequest request) {
    try {
      // Check if the email provided by the user is correct
      Result<Boolean, HttpException> emailResult = new CheckEmailCommand(request.getUsername())
          .execute();

      // If it's not correct then create an appropriate response based on the error
      if (!emailResult.haveData()) {
        HttpException e = emailResult.getException();
        return new BuildResponseFromException(e).build().getData();
      }

      // If the username is already in use
      if (userRepository.existsByEmail(request.getUsername())) {
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Username is already in use!"));
      }

      // Create the entity, encode the password and save it in the database
      UserEntity user = new UserEntity(
          request.getName(),
          request.getUsername(),
          this.passwordEncoder.encode(request.getPassword())
      );

      this.userRepository.save(user);

      // Return an ok response
      return ResponseEntity.ok(
          new MessageResponse("User registered successfully!")
      );
    } catch (Exception e) {
      logger.error("SignUpController::signup Failed to execute. Error: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
