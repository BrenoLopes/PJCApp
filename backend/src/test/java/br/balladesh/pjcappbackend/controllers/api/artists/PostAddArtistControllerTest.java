package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.api.artists.AddArtistRequestDTO;
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
class PostAddArtistControllerTest {
  @Mock
  private ArtistsService artistsService;
  @Mock
  private UsersService usersService;

  private PostAddArtistController testTarget;

  @BeforeEach
  void setUp() {
    this.testTarget = new PostAddArtistController(this.artistsService, this.usersService);
  }

  private final UserEntity robotUser = new UserEntity(
      "robot",
      "robot@robot.com",
      "123456"
  );

  @Test
  void forbidden_NoUserAuthenticated_WhenAddingArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<?> response = testTarget.addArtist(new AddArtistRequestDTO("RandomName"));
    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_DbFailed_WhenAddingArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.robotUser));

    Mockito
        .when(this.artistsService.addArtist(anyString(), any()))
        .thenThrow(new InternalServerErrorException("Whoops"));

    ResponseEntity<?> response = testTarget.addArtist(new AddArtistRequestDTO("RandomName"));

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void internalServerError_DbFailedToAdd_WhenAddingArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.robotUser));

    Mockito
        .when(this.artistsService.addArtist(anyString(), any()))
        .thenReturn(false);

    ResponseEntity<?> response = testTarget.addArtist(new AddArtistRequestDTO("RandomName"));

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_WhenAddingArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.robotUser));

    Mockito
        .when(this.artistsService.addArtist(anyString(), any()))
        .thenReturn(true);

    ResponseEntity<?> result = this.testTarget.addArtist(new AddArtistRequestDTO("RandomName"));

    assertSame(HttpStatus.OK, result.getStatusCode());
  }
}