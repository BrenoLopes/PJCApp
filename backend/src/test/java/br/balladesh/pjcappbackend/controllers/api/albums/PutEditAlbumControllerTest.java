package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.EditAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.defaults.EmptyMultipartFile;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PutEditAlbumControllerTest {
  @Mock
  private AlbumRepository albumRepository;

  @Mock
  private ArtistRepository artistRepository;

  @Autowired
  private MinIOEndpoint minIOEndpoint;

  @Test
  void testWithNullRepository() {
    PutEditAlbumController testTarget = new PutEditAlbumController(null, null, null);
    ResponseEntity<MessageResponse> result = testTarget.editAlbum(null);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }

  @Test
  void testWithNotFoundData() {
    PutEditAlbumController testTarget = new PutEditAlbumController(
        this.albumRepository,
        this.artistRepository,
        this.minIOEndpoint
    );

    EditAlbumRequestBody request = new EditAlbumRequestBody(
        1L,
        "ohno1",
        new EmptyMultipartFile(),
        1L
    );

    Mockito.when(this.albumRepository.findById(request.getAlbumId()))
        .thenReturn(Optional.empty());

    ResponseEntity<MessageResponse> response = testTarget.editAlbum(request);

    assertSame(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testWithInvalidArtist() {
    PutEditAlbumController testTarget = new PutEditAlbumController(
        this.albumRepository,
        this.artistRepository,
        this.minIOEndpoint
    );
    EditAlbumRequestBody request = new EditAlbumRequestBody(
        1L,
        "ohno1",
        new EmptyMultipartFile(),
        1L
    );

    AlbumEntity album = new AlbumEntity(
        1L,
        "ohno",
        new ArtistEntity("amongus", Lists.newArrayList()),
        ""
    );

    Mockito.when(this.albumRepository.findById(request.getAlbumId()))
        .thenReturn(Optional.of(album));
    Mockito.when(this.artistRepository.findById(request.getArtistId()))
        .thenReturn(Optional.empty());

    ResponseEntity<MessageResponse> response = testTarget.editAlbum(request);

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testWithValidArtist() {
    PutEditAlbumController testTarget = new PutEditAlbumController(
        this.albumRepository,
        this.artistRepository,
        this.minIOEndpoint
    );
    EditAlbumRequestBody request = new EditAlbumRequestBody(
        1L,
        "ohno1",
        new EmptyMultipartFile(),
        1L
    );

    AlbumEntity album = new AlbumEntity(
        1L,
        "ohno",
        new ArtistEntity(1L, "amongus", Lists.newArrayList()),
        ""
    );
    ArtistEntity artist = new ArtistEntity(
        1L,
        "amongus",
        Lists.newArrayList()
    );

    Mockito.when(this.albumRepository.findById(request.getAlbumId()))
        .thenReturn(Optional.of(album));
    Mockito.when(this.artistRepository.findById(request.getArtistId()))
        .thenReturn(Optional.of(artist));

    album.setArtist(artist);
    album.setName(request.getName());
    Mockito.when(this.albumRepository.save(album)).thenReturn(album);

    ResponseEntity<MessageResponse> response = testTarget.editAlbum(request);

    assertSame(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testWithValidImage() {
    PutEditAlbumController testTarget = new PutEditAlbumController(
        this.albumRepository,
        this.artistRepository,
        this.minIOEndpoint
    );
    EditAlbumRequestBody request = new EditAlbumRequestBody(
        1L,
        "ohno1",
        new MockMultipartFile("OHNO.png", "OHNO.png", "images/png", new byte[0]),
        1L
    );

    AlbumEntity album = new AlbumEntity(
        1L,
        "ohno",
        new ArtistEntity(1L, "amongus", Lists.newArrayList()),
        ""
    );
    ArtistEntity artist = new ArtistEntity(
        1L,
        "amongus",
        Lists.newArrayList()
    );

    Mockito.when(this.albumRepository.findById(request.getAlbumId()))
        .thenReturn(Optional.of(album));
    Mockito.when(this.artistRepository.findById(request.getArtistId()))
        .thenReturn(Optional.of(artist));

    album.setArtist(artist);
    album.setName(request.getName());

    String fileName = String.format("%d-OHNO.png", System.currentTimeMillis());
    album.setImage(fileName);

    Mockito.when(this.albumRepository.save(album)).thenReturn(album);
    ResponseEntity<MessageResponse> response = testTarget.editAlbum(request);

    assertSame(HttpStatus.OK, response.getStatusCode());
  }
}