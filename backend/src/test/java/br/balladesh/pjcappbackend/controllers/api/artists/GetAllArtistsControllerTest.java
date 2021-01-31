package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.api.artists.PagedArtistResponseBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetAllArtistsControllerTest {
  @Mock
  private ArtistRepository artistRepository;

  @Test
  void testNullRepository() {
    GetAllArtistsController testTarget = new GetAllArtistsController(null);
    ResponseEntity<?> result = testTarget.getAllArtists(0, 10, "asc");

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }

  @Test
  void testWithValidRepository() {
    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
    ArtistEntity artist1 = new ArtistEntity(1L, "ohno1", Lists.newArrayList(), robotUser);
    ArtistEntity artist2 = new ArtistEntity(2L, "ohno2", Lists.newArrayList(), robotUser);

    Pageable page = PageRequest.of(0, 10, Sort.by("name"));

    Page<ArtistEntity> aPage = new PageImpl<>(Lists.newArrayList(artist1, artist2));
    Mockito.when(this.artistRepository.findAll(page)).thenReturn(aPage);

    PagedArtistResponseBody expected = new PagedArtistResponseBody(aPage);

    GetAllArtistsController testTarget = new GetAllArtistsController(this.artistRepository);
    ResponseEntity<PagedArtistResponseBody> result = (ResponseEntity<PagedArtistResponseBody>)
      testTarget.getAllArtists(0, 10, "asc");

    assertSame(HttpStatus.OK, result.getStatusCode());
    assertEquals(expected, result.getBody());
  }
}