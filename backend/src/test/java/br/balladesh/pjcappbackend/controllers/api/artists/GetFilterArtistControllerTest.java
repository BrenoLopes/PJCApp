package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.api.artists.PagedArtistResponseBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GetFilterArtistControllerTest {
  @Mock
  private ArtistRepository artistRepository;

  @Test
  void testNullRepository() {
    GetFilterArtistController testTarget = new GetFilterArtistController(null);
    ResponseEntity<?> result = testTarget.filterArtistBy(1L,null, 0, 10, "asc");

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }

  @Test
  void testWithValidRepository() {
    long id = 1L;
    String name = "ohno2";

    ArtistEntity artist1 = new ArtistEntity(1L, "ohno1", Lists.newArrayList());
    ArtistEntity artist2 = new ArtistEntity(2L, "ohno2", Lists.newArrayList());
    List<ArtistEntity> artists = Lists.newArrayList(artist1);

    Page<ArtistEntity> aPage = new PageImpl<>(artists);

    Pageable page = PageRequest.of(0, 10, Sort.by("name"));
    Mockito.when(this.artistRepository.findByIdAndName(id, null, page))
        .thenReturn(aPage);

    PagedArtistResponseBody expected = new PagedArtistResponseBody(aPage);

    GetFilterArtistController testTarget = new GetFilterArtistController(this.artistRepository);
    ResponseEntity<PagedArtistResponseBody> result = (ResponseEntity<PagedArtistResponseBody>)
        testTarget.filterArtistBy( id, null, 0, 10, "asc");

    assertSame(HttpStatus.OK, result.getStatusCode());
    assertEquals(expected, result.getBody());
  }
}