package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.EditAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PutEditAlbumControllerTest {
  @Mock
  private AlbumsService albumsService;
  @Mock
  private UsersService usersService;
  @Mock
  private ArtistsService artistsService;

  private PutEditAlbumController testTarget;

  private final EditAlbumRequestBody request = new EditAlbumRequestBody(
      1L,
      "TheNewAlbum",
      new MockMultipartFile("TheAlbum", "TheAlbum", "image/png", new byte[1]),
      1L
  );

  @BeforeEach
  void setUp() {
    this.testTarget = new PutEditAlbumController(this.albumsService, this.usersService, this.artistsService);
  }

  private final UserEntity currentUser = new UserEntity(
      "TheRobot",
      "robot@robocop.com",
      "123456BCrypted"
  );

  @Test
  void badRequest_WhenEditingAnAlbum_NoId() {
    ResponseEntity<MessageResponse> response = testTarget.editAlbum(null);

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void forbidden_WhenEditingAnAlbum_BecauseTheUserHaventLoaded() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<MessageResponse> response = testTarget.editAlbum(this.request);

    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_WhenEditingAnAlbum_BecauseTheDbDied() {
    ArtistEntity theArtist = new ArtistEntity("wooo", this.currentUser);

    AlbumEntity theAlbum = new AlbumEntity("aaa", "");
    theAlbum.setArtist(theArtist);

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .when(this.albumsService.searchAnAlbumWithoutMinIO(this.request.getAlbumId(), this.currentUser))
        .thenReturn(theAlbum);

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.albumsService).setAnAlbum(this.request.getAlbumId(), this.currentUser, this.request.getName(), this.request.getImage());

    ResponseEntity<MessageResponse> response = testTarget.editAlbum(this.request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_WhenEditingAnAlbum() {
    ArtistEntity theArtist = new ArtistEntity("wooo", new ArrayList<>(), this.currentUser);

    AlbumEntity theAlbum = new AlbumEntity("aaa", "");
    theAlbum.setArtist(theArtist);

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .when(this.albumsService.searchAnAlbumWithoutMinIO(this.request.getAlbumId(), this.currentUser))
        .thenReturn(theAlbum);

    Mockito
        .doNothing()
        .when(this.albumsService).setAnAlbum(this.request.getAlbumId(), this.currentUser, this.request.getName(), this.request.getImage());

    ResponseEntity<MessageResponse> response = testTarget.editAlbum(this.request);

    assertSame(HttpStatus.OK, response.getStatusCode());
  }
}