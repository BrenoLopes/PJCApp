package br.balladesh.pjcappbackend.controllers.security.signup;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.security.UserSignUpRequest;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {
  @Mock
  private UsersService usersService;

  @Mock
  private ArtistsService artistsService;

  @Mock
  private AlbumsService albumsService;

  @Test
  void shouldReturnOk() throws BadRequestException, ConflictException, InternalServerErrorException {
    UserSignUpRequest request = new UserSignUpRequest(
        "robot@robocop.com",
        "Mr. Robot",
        "123456"
    );

    UserEntity expected = new UserEntity(1L, request.getName(), request.getUsername(), request.getPassword(), new ArrayList<>());

    Mockito
        .when(
            this.usersService
                .addUser(
                    request.getName(),
                    request.getUsername(),
                    request.getPassword(),
                    Lists.newArrayList()
                )
        )
        .thenReturn(expected);

    SignUpController testTarget = new SignUpController(this.usersService, artistsService, albumsService);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(request);

    assertSame(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void shouldReturnBadRequest_NoRequest() {
    SignUpController testTarget = new SignUpController(this.usersService, artistsService, albumsService);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(null);

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void shouldReturnBadRequest_InvalidEmail() throws BadRequestException, ConflictException, InternalServerErrorException {
    UserSignUpRequest request = new UserSignUpRequest(
        "robot",
        "Mr. Robot",
        "123456"
    );

    Mockito
        .doThrow(new BadRequestException("Whoops"))
        .when(this.usersService)
        .addUser(request.getName(), request.getUsername(), request.getPassword(), Lists.newArrayList());

    SignUpController testTarget = new SignUpController(this.usersService, artistsService, albumsService);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(request);

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void shouldReturnConflict_UserAlreadyExists() throws BadRequestException, InternalServerErrorException, ConflictException {
    UserSignUpRequest request = new UserSignUpRequest(
        "robot@robocop.com",
        "Mr. Robot",
        "123456"
    );

    Mockito
        .doThrow(new ConflictException("Whoops!"))
        .when(this.usersService)
        .addUser(request.getName(), request.getUsername(), request.getPassword(), Lists.newArrayList());


    SignUpController testTarget = new SignUpController(this.usersService, artistsService, albumsService);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(request);

    assertSame(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  void shouldReturnInternalServerError_FailedConnectDB() throws BadRequestException, InternalServerErrorException {
    UserSignUpRequest request = new UserSignUpRequest(
        "robot@robocop.com",
        "Mr. Robot",
        "123456"
    );

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.usersService)
        .addUser(request.getName(), request.getUsername(), request.getPassword(), Lists.newArrayList());

    SignUpController testTarget = new SignUpController(this.usersService, artistsService, albumsService);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}