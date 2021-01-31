package br.balladesh.pjcappbackend.controllers.security.signup;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.security.UserSignUpRequest;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class SignUpController {
  private final UsersService usersService;
  private final Logger logger = LoggerFactory.getLogger(SignUpController.class);

  @Autowired
  public SignUpController(UsersService usersService) {
    this.usersService = usersService;
  }

  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(@RequestBody UserSignUpRequest request) {
    if (request == null)
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    try {
      this.usersService.addUser(
          request.getName(),
          request.getUsername(),
          request.getPassword(),
          Lists.newArrayList()
      );

      return ResponseCreator.create(HttpStatus.OK);
    } catch (InternalServerErrorException e) {
      this.logger.error("SignUpController::registerUser Failed to create an user. Error: {}", e.getMessage());
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (BadRequestException e) {
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);
    } catch (ConflictException e) {
      return ResponseCreator.create(HttpStatus.CONFLICT);
    }
  }
}
