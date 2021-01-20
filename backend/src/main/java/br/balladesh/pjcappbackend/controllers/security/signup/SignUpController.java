package br.balladesh.pjcappbackend.controllers.security.signup;

import br.balladesh.pjcappbackend.dto.security.UserSignUpRequest;
import br.balladesh.pjcappbackend.entity.security.UserEntity;
import br.balladesh.pjcappbackend.repository.security.UserRepository;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.Result;

import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.NonNull;
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
  private final Logger logger = LoggerFactory.getLogger(SignUpController.class);

  @Autowired
  public SignUpController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody UserSignUpRequest request) {
    if (NonNull.withParams(this.passwordEncoder, this.userRepository).check()) {
      this.logger.error("SignUpController::registerUser Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      // Check if the email provided by the user is correct
      Result<Boolean, HttpException> checkResult = new CheckEmailCommand(request.getUsername())
          .execute();

      // If it's not correct then create an appropriate response based on the error
      if (!checkResult.haveData())
        return ResponseCreator.create(checkResult.getException().getStatusCode());

      // If the username is already in use
      if ( userRepository.existsByEmail(request.getUsername()) )
        return ResponseCreator.create(HttpStatus.CONFLICT);

      // Create the entity, encode the password and save it in the database
      UserEntity user = new UserEntity(
          request.getName(),
          request.getUsername(),
          this.passwordEncoder.encode(request.getPassword())
      );

      this.userRepository.save(user);

      return ResponseCreator.create(HttpStatus.OK);
    } catch (Exception e) {
      logger.error("SignUpController::signup Failed to execute. Error: {}", e.getMessage());
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
