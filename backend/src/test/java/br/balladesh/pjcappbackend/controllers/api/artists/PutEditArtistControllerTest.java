package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.api.artists.PutArtistRequestDTO;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import com.google.common.collect.Lists;
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
class PutEditArtistControllerTest {
  @Mock
  private ArtistRepository artistRepository;

  @Test
  void testNullRepository() {
    PutArtistRequestDTO request = new PutArtistRequestDTO(1L, "ohno1");
    PutEditArtistController testTarget = new PutEditArtistController(null);
    ResponseEntity<?> result = testTarget.editArtist(request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }

  @Test
  void testEditNonExistentArtist() {
    PutArtistRequestDTO request = new PutArtistRequestDTO(1L, "ohno1");
    PutEditArtistController testTarget = new PutEditArtistController(this.artistRepository);

    Mockito.when(this.artistRepository.findById(1L)).thenReturn(Optional.empty());

    ResponseEntity<?> result = testTarget.editArtist(request);

    assertSame(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void testEditExistentArtist() {
    PutArtistRequestDTO request = new PutArtistRequestDTO(1L, "ohno1");
    PutEditArtistController testTarget = new PutEditArtistController(this.artistRepository);

    ArtistEntity entity = new ArtistEntity(1L, "ohno", Lists.newArrayList());
    Mockito.when(this.artistRepository.findById(1L)).thenReturn(Optional.of(entity));
    Mockito.when(this.artistRepository.save(entity))
        .thenReturn(new ArtistEntity(1L, "ohno1", Lists.newArrayList()));

    ResponseEntity<?> result = testTarget.editArtist(request);

    assertSame(HttpStatus.OK, result.getStatusCode());
  }
}
