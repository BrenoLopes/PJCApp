package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DeleteAlbumControllerTest {
  @Mock
  private AlbumRepository albumRepository;

  @Autowired
  private MinIOEndpoint endpoint;

  @Test
  void testWithNullRepository() {
    DeleteAlbumController testTarget = new DeleteAlbumController(null, endpoint);
    ResponseEntity<MessageResponse> result = testTarget.deleteAlbum(Optional.of(1L));

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }

  @Test
  void testWithNullId() {
    DeleteAlbumController testTarget = new DeleteAlbumController(this.albumRepository, endpoint);
    ResponseEntity<MessageResponse> result = testTarget.deleteAlbum(Optional.empty());

    assertSame(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void testWithAlbumNotFound() {
    DeleteAlbumController testTarget = new DeleteAlbumController(this.albumRepository, endpoint);

    Mockito.when(albumRepository.findById(2L)).thenReturn(Optional.empty());
    ResponseEntity<MessageResponse> result = testTarget.deleteAlbum(Optional.of(2L));

    assertSame(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void testWithValidAlbum() {
    AlbumEntity albumEntity = new AlbumEntity(
        2L,
        "AmongUs",
        new ArtistEntity("OhNo", Lists.newArrayList()),
        ""
    );
    DeleteAlbumController testTarget = new DeleteAlbumController(this.albumRepository, endpoint);

    Mockito.when(albumRepository.findById(2L)).thenReturn(Optional.of(albumEntity));
    Mockito.doNothing().when(albumRepository).delete(albumEntity);
    ResponseEntity<MessageResponse> result = testTarget.deleteAlbum(Optional.of(2L));

    assertSame(HttpStatus.OK, result.getStatusCode());
  }
}