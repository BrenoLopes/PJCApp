package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.artists.PutArtistRequestDTO;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PutEditArtistControllerTest {
  @Mock
  private ArtistsService artistsService;
  @Mock
  private UsersService usersService;

  private PutEditArtistController testTarget;

  @BeforeEach
  void setUp() {
    this.testTarget = new PutEditArtistController(this.artistsService, this.usersService);
  }

  private final UserEntity robotUser = new UserEntity(
      "robot",
      "robot@robot.com",
      "123456"
  );

  private final PutArtistRequestDTO request = new PutArtistRequestDTO(1L, "NewName");

  @Test
  void forbidden_NoUserAuthenticated_WhenEditingAnArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<MessageResponse> response = testTarget.editArtist(request);
    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_DbFailed_WhenEditingAnArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.robotUser));

    Mockito
        .lenient()
        .when(this.artistsService.setAnArtist(anyInt(), Optional.of(anyString()), any(), any()))
        .thenThrow(new InternalServerErrorException("Whoops"));

    ResponseEntity<MessageResponse> response = testTarget.editArtist(request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void internalServerError_DbFailedToAdd_WhenEditingAnArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.robotUser));

    Mockito
        .lenient()
        .when(this.artistsService.setAnArtist(anyInt(), Optional.of(anyString()), any(), any()))
        .thenReturn(false);

    ResponseEntity<MessageResponse> response = testTarget.editArtist(request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_WhenEditingAnArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.robotUser));

    Mockito
        .lenient()
        .when(this.artistsService.setAnArtist(
            request.getId(),
            Optional.of(request.getName()),
            Optional.empty(),
            this.robotUser)
        )
        .thenReturn(true);

    ResponseEntity<MessageResponse> result = this.testTarget.editArtist(request);

    assertSame(HttpStatus.OK, result.getStatusCode());
  }
}
