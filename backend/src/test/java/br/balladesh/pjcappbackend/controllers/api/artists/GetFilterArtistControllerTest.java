package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetFilterArtistControllerTest {
  @Mock
  private ArtistsService artistsService;
  @Mock
  private UsersService usersService;

  private GetFilterArtistController testTarget;

  private final UserEntity userEntity = new UserEntity(
      "TheRobot",
      "robot@robocop.com",
      "123456BCrypted"
  );

  @BeforeEach
  private void setUp() {
    this.testTarget = new GetFilterArtistController(this.artistsService, this.usersService);
  }

  @Test
  void badRequest_filteringAnArtist_NoParameter() {
    ResponseEntity<?> response = testTarget.filterArtistBy(null, null);
    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void forbidden_filteringAnArtist_BecauseTheUserHaventLoaded() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<?> response = testTarget.filterArtistBy(1L, null);

    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_filteringAnArtist_BecauseTheDbDied() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(userEntity));

    final long theId = 1;

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistsService).searchAnArtist(theId, userEntity);

    ResponseEntity<?> response = testTarget.filterArtistBy(theId, null);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_filteringAnArtist() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(userEntity));

    final long theId = 1;

    ArtistEntity expected = new ArtistEntity(theId, "Robot", new ArrayList<>(), this.userEntity);

    Mockito
        .doReturn(expected)
        .when(this.artistsService).searchAnArtist(theId, userEntity);

    ResponseEntity<?> response = testTarget.filterArtistBy(theId, null);

    assertSame(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody() instanceof ArtistEntity);
    assertEquals(expected, response.getBody());
  }
}