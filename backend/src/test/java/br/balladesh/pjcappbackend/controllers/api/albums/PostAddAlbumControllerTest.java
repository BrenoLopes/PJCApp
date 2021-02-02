package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.AddAlbumRequestBody;
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
class PostAddAlbumControllerTest {
  @Mock
  private AlbumsService albumsService;
  @Mock
  private UsersService usersService;
  @Mock
  private ArtistsService artistsService;

  private final AddAlbumRequestBody request = new AddAlbumRequestBody(
      "AndAnotherOne",
      new MockMultipartFile("thefile", "thefile", "image/png", new byte[1]),
      1L
  );

  private PostAddAlbumController testTarget;

  @BeforeEach
  void setUp() {
    this.testTarget = new PostAddAlbumController(
        this.albumsService, 
        this.usersService,
        this.artistsService
    );
  }

  private final UserEntity currentUser = new UserEntity(
      "TheRobot",
      "robot@robocop.com",
      "123456BCrypted"
  );

  @Test
  void badRequest_WhenAddingAnAlbum_NoArgs() {
    ResponseEntity<MessageResponse> response = testTarget.addAlbum(null);

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void forbidden_WhenAddingAnAlbum_BecauseTheUserHaventLoaded() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(this.request);

    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void forbidden_WhenAddingAnAlbum_BecauseTheUserIsUsingAnArtistFromAnotherUser() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    UserEntity anotherUser = new UserEntity("what", "what@email.com", "123456");
    ArtistEntity theArtist = new ArtistEntity("wooo", new ArrayList<>(), anotherUser);
    Mockito
        .when(this.artistsService.searchAnArtist(this.request.getArtistId(), this.currentUser))
        .thenReturn(theArtist);
    
    ResponseEntity<MessageResponse> response = testTarget.addAlbum(this.request);
    
    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void notFound_WhenAddingAnAlbum_BecauseTheArtistDoesntExists() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .doThrow(new NotFoundException("Whoops"))
        .when(this.artistsService).searchAnArtist(this.request.getArtistId(), this.currentUser);

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(this.request);

    assertSame(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void internalServerError_WhenAddingAnAlbum_BecauseTheDbDied() {
    ArtistEntity theArtist = new ArtistEntity("wooo", new ArrayList<>(), this.currentUser);

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .when(this.artistsService.searchAnArtist(this.request.getArtistId(), this.currentUser))
        .thenReturn(theArtist);

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.albumsService).addAnAlbum(theArtist, this.currentUser, this.request.getName(), this.request.getImage());

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(this.request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void conflict_WhenAddingAnAlbum_BecauseTheDbDied() {
    ArtistEntity theArtist = new ArtistEntity("wooo", new ArrayList<>(), this.currentUser);

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .when(this.artistsService.searchAnArtist(this.request.getArtistId(), this.currentUser))
        .thenReturn(theArtist);

    Mockito
        .doThrow(new ConflictException("Whoops"))
        .when(this.albumsService).addAnAlbum(theArtist, this.currentUser, this.request.getName(), this.request.getImage());

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(this.request);

    assertSame(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  void success_WhenAddingAnAlbum() {
    ArtistEntity theArtist = new ArtistEntity("wooo", new ArrayList<>(), this.currentUser);

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .when(this.artistsService.searchAnArtist(this.request.getArtistId(), this.currentUser))
        .thenReturn(theArtist);

    Mockito
        .when(this.albumsService.addAnAlbum(theArtist, this.currentUser, this.request.getName(), this.request.getImage()))
        .thenReturn(true);

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(this.request);

    assertSame(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void internalServerError_WhenReturningFalseWhenAddingAnAlbum() {
    ArtistEntity theArtist = new ArtistEntity("wooo", new ArrayList<>(), this.currentUser);

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .when(this.artistsService.searchAnArtist(this.request.getArtistId(), this.currentUser))
        .thenReturn(theArtist);

    Mockito
        .when(this.albumsService.addAnAlbum(theArtist, this.currentUser, this.request.getName(), this.request.getImage()))
        .thenReturn(false);

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(this.request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}