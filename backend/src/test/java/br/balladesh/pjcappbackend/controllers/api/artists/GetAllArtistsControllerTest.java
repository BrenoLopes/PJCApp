package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.api.artists.PagedArtistResponseBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetAllArtistsControllerTest {
  @Mock
  private ArtistsService artistsService;

  @Mock
  private UsersService usersService;

  private GetAllArtistsController testTarget;

  private final UserEntity userEntity = new UserEntity(
      "TheRobot",
      "robot@robocop.com",
      "123456BCrypted"
  );

  @BeforeEach
  void setUp() {
    this.testTarget = new GetAllArtistsController(this.artistsService, this.usersService);
  }

  @Test
  void forbidden_NoUserAuthenticated_WhenGettingAllArtists() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<?> response = testTarget.getAllArtists(0, 10, "asc");

    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_DbFailed_WhenGettingAllArtists() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.userEntity));

    Mockito
        .when(this.artistsService.getAllArtists(any(), anyInt(), anyInt(), anyString()))
        .thenThrow(new InternalServerErrorException("Whoops"));

    ResponseEntity<?> response = testTarget.getAllArtists(0, 10, "asc");

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_WhenGettingAllArtists() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.userEntity));

    List<ArtistEntity> expectedList = Lists.newArrayList(
        new ArtistEntity("Artist1", new ArrayList<>(), this.userEntity),
        new ArtistEntity("Artist2", new ArrayList<>(), this.userEntity)
    );
    Page<ArtistEntity> expectedPage = new PageImpl<>(
        expectedList,
        PageRequest.of(0, 10, Sort.by("name")),
        expectedList.size()
    );

    Mockito
        .when(this.artistsService.getAllArtists(any(), anyInt(), anyInt(), anyString()))
        .thenReturn(expectedPage);

    ResponseEntity<?> result = this.testTarget.getAllArtists(0, 10, "asc");

    assertTrue(result.getBody() instanceof PagedArtistResponseBody);
    assertSame(HttpStatus.OK, result.getStatusCode());

    PagedArtistResponseBody expected = new PagedArtistResponseBody(expectedPage);
    PagedArtistResponseBody received = (PagedArtistResponseBody) result.getBody();
    assertEquals(expected, received);
  }
}