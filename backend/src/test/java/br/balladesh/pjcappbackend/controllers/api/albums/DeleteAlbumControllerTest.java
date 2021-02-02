package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
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

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DeleteAlbumControllerTest {
  @Mock
  private AlbumsService albumsService;
  @Mock
  private UsersService usersService;

  private DeleteAlbumController testTarget;

  @BeforeEach
  void setUp() {
    this.testTarget = new DeleteAlbumController(this.albumsService, this.usersService);
  }

  private final UserEntity currentUser = new UserEntity(
      "TheRobot",
      "robot@robocop.com",
      "123456BCrypted"
  );

  @Test
  void badRequest_WhenDeletingAnAlbum_NoId() {
    ResponseEntity<MessageResponse> response = testTarget.deleteAlbum(Optional.empty());

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void forbidden_WhenDeletingAnAlbum_BecauseTheUserHaventLoaded() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<MessageResponse> response = testTarget.deleteAlbum(Optional.of(1L));

    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_WhenDeletingAnAlbum_BecauseTheDbDied() {
    final long theId = 1;

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.albumsService).removeAnAlbum(theId, currentUser);

    ResponseEntity<MessageResponse> response = testTarget.deleteAlbum(Optional.of(theId));

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_WhenDeletingAnAlbum() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    final long theId = 1;

    Mockito
        .doNothing()
        .when(this.albumsService).removeAnAlbum(theId, this.currentUser);

    ResponseEntity<MessageResponse> response = testTarget.deleteAlbum(Optional.of(theId));

    assertSame(HttpStatus.OK, response.getStatusCode());
  }
}