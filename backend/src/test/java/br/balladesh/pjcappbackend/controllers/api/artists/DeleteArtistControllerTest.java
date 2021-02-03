package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
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

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DeleteArtistControllerTest {
  @Mock
  private ArtistsService artistsService;

  @Mock
  private UsersService usersService;

  @Test
  void badRequest_WhenDeletingAnArtist_NoId() {
    DeleteArtistController testTarget = new DeleteArtistController(this.artistsService, this.usersService);

    ResponseEntity<MessageResponse> response = testTarget.deleteArtist(Optional.empty());

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void forbidden_WhenDeletingAnArtist_BecauseTheUserHaventLoaded() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    DeleteArtistController testTarget = new DeleteArtistController(this.artistsService, this.usersService);
    ResponseEntity<MessageResponse> response = testTarget.deleteArtist(Optional.of(1L));

    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_WhenDeletingAnArtist_BecauseTheDbDied() {
    UserEntity userEntity = new UserEntity(
        "TheRobot",
        "robot@robocop.com",
        "123456BCrypted"
    );

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(userEntity));

    final long theId = 1;

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistsService).removeAnArtist(theId, userEntity);

    DeleteArtistController testTarget = new DeleteArtistController(this.artistsService, this.usersService);
    ResponseEntity<MessageResponse> response = testTarget.deleteArtist(Optional.of(theId));

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_WhenDeletingAnArtist() {
    UserEntity userEntity = new UserEntity(
        "TheRobot",
        "robot@robocop.com",
        "123456BCrypted"
    );

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(userEntity));

    final long theId = 1;

    Mockito
        .doNothing()
        .when(this.artistsService).removeAnArtist(theId, userEntity);

    DeleteArtistController testTarget = new DeleteArtistController(this.artistsService, this.usersService);
    ResponseEntity<MessageResponse> response = testTarget.deleteArtist(Optional.of(theId));

    assertSame(HttpStatus.OK, response.getStatusCode());
  }
}