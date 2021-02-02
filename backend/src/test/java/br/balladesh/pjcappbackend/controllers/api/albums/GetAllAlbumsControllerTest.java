package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetAllAlbumsControllerTest {
  @Mock
  private AlbumsService albumsService;
  @Mock
  private UsersService usersService;

  private GetAllAlbumsController testTarget;

  @BeforeEach
  void setUp() {
    this.testTarget = new GetAllAlbumsController(this.albumsService, this.usersService);
  }

  private final UserEntity currentUser = new UserEntity(
      "TheRobot",
      "robot@robocop.com",
      "123456BCrypted"
  );

  @Test
  void forbidden_WhenGettingAllAlbum_BecauseTheUserHaventLoaded() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.empty());

    ResponseEntity<?> response = testTarget.getAllAlbums(
        1L,
        0,
        10,
        "asc"
    );

    assertSame(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void internalServerError_WhenGettingAllAlbum_BecauseTheDbDied() {
    final long theId = 1;

    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.albumsService).getAllAlbumsFromArtist(1L, this.currentUser, 0, 10, "asc");

    ResponseEntity<?> response = testTarget.getAllAlbums(
        1L,
        0,
        10,
        "asc"
    );

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void success_WhenGettingAllAlbum() {
    Mockito
        .when(this.usersService.getCurrentAuthenticatedUser())
        .thenReturn(Optional.of(this.currentUser));

    final long theId = 1;

    Page<AlbumEntity> expectedPage = this.createDummyPage();

    Mockito
        .doReturn(expectedPage)
        .when(this.albumsService).getAllAlbumsFromArtist(theId, this.currentUser, 0, 10, "asc");

    ResponseEntity<?> response = testTarget.getAllAlbums(theId, 0, 10, "asc");

    assertTrue(response.getBody() instanceof PagedAlbumResponseBody);
    assertSame(HttpStatus.OK, response.getStatusCode());
    assertEquals(new PagedAlbumResponseBody(expectedPage), response.getBody());
  }

  private Page<AlbumEntity> createDummyPage() {
    ArtistEntity artist = new ArtistEntity("Artist1", new ArrayList<>(), this.currentUser);

    AlbumEntity album1 = new AlbumEntity("Album1", artist, "123-Image1");
    AlbumEntity album2 = new AlbumEntity("Album2", artist, "123-Image2");
    AlbumEntity album3 = new AlbumEntity("Album3", artist, "123-Image3");
    AlbumEntity album4 = new AlbumEntity("Album4", artist, "123-Image4");

    ArrayList<AlbumEntity> albumList = Lists.newArrayList(album1, album2, album3, album4);

    return new PageImpl<>(albumList, PageRequest.of(0, 10, Sort.by("name")), albumList.size());
  }
}