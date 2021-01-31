package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.minio.GetFromMinIOCommand;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.utilities.Result;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetAllAlbumsControllerTest {
  @Mock
  private AlbumRepository albumRepository;

  @Mock
  private GetFromMinIOCommand command;

  @Autowired
  private MinIOEndpoint endpoint;

  @Test
  void testWithNullRepository() {
    GetAllAlbumsController testTarget = new GetAllAlbumsController(null, endpoint);
    ResponseEntity<?> _result = testTarget.getAllAlbums(0, 10, "asc");

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, _result.getStatusCode());
  }

  @Test
  void testEmptyList() {
    List<AlbumEntity> albumList = Lists.newArrayList();
    Page<AlbumEntity> returnPage = new PageImpl<>(albumList);
    Pageable inputPage = PageRequest.of(0, 10, Sort.by("name"));

    Mockito.when(this.albumRepository.findAll(inputPage)).thenReturn(returnPage);

    GetAllAlbumsController testTarget = new GetAllAlbumsController(this.albumRepository, endpoint, this.command);
    ResponseEntity<?> _result = testTarget.getAllAlbums(0, 10, "asc");

    if (!(_result.getBody() instanceof PagedAlbumResponseBody))
      fail();

    PagedAlbumResponseBody result = (PagedAlbumResponseBody) _result.getBody();

    assertSame(HttpStatus.OK, _result.getStatusCode());
    assertTrue(result.getAlbums().size() < 1);
  }

  @Test
  void testListing() {
    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
    List<AlbumEntity> albumList = Lists.newArrayList(
        new AlbumEntity(1L, "Ohno1", new ArtistEntity("AH1", Lists.newArrayList(), robotUser), ""),
        new AlbumEntity(2L, "Ohno2", new ArtistEntity("AH2", Lists.newArrayList(), robotUser), ""),
        new AlbumEntity(3L, "Ohno3", new ArtistEntity("AH3", Lists.newArrayList(), robotUser), ""),
        new AlbumEntity(4L, "Ohno4", new ArtistEntity("AH4", Lists.newArrayList(), robotUser), "")
    );
    Page<AlbumEntity> returnPage = new PageImpl<>(albumList);
    Pageable inputPage = PageRequest.of(0, 10, Sort.by("name"));

    Mockito.when(this.albumRepository.findAll(inputPage)).thenReturn(returnPage);
    Mockito.when(this.command.execute()).thenReturn(Result.from("MockMinIOObject"));

    GetAllAlbumsController testTarget = new GetAllAlbumsController(this.albumRepository, endpoint, this.command);
    ResponseEntity<?> _result = testTarget.getAllAlbums(0, 10, "asc");

    if (!(_result.getBody() instanceof PagedAlbumResponseBody))
      fail();

    PagedAlbumResponseBody result = (PagedAlbumResponseBody) _result.getBody();

    albumList = albumList
        .stream()
        .peek(album -> album.setImage("MockMinIOObject"))
        .collect(Collectors.toList());
    Page<AlbumEntity> expected = new PageImpl<>(albumList);

    assertSame(HttpStatus.OK, _result.getStatusCode());
    assertEquals(new PagedAlbumResponseBody(expected), result);
  }
}