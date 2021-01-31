package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.AddAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.security.UserEntity;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PostAddAlbumControllerTest {
  @Mock
  private AlbumRepository albumRepository;

  @Mock
  private ArtistRepository artistRepository;

  @Autowired
  private MinIOEndpoint endpoint;

  @Test
  void testWithNullRepository() {
    PostAddAlbumController testTarget = new PostAddAlbumController(null, null, null);
    ResponseEntity<MessageResponse> result = testTarget.addAlbum(null);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }

  @Test
  void testAlbumAlreadyExists() {
    AddAlbumRequestBody request = new AddAlbumRequestBody("ohno", new EmptyMultipartFile(), 1L, "AmongUs");
    PostAddAlbumController testTarget = new PostAddAlbumController(this.albumRepository, this.artistRepository, this.endpoint);

    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
    ArtistEntity artistEntity = new ArtistEntity(1L, "AmongUs", Lists.newArrayList(), robotUser);
    AlbumEntity albumEntity = new AlbumEntity(1L, "ohno", artistEntity, "");

    Mockito.when(this.albumRepository.findByName(request.getName()))
        .thenReturn(Optional.of(albumEntity));

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(request);

    assertSame(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  void testNonExistentAlbumWithNonExistentArtist() {
    AddAlbumRequestBody request = new AddAlbumRequestBody("ohno", new EmptyMultipartFile(), 1L, "AmongUs");
    PostAddAlbumController testTarget = new PostAddAlbumController(this.albumRepository, this.artistRepository, this.endpoint);

    Mockito.when(this.albumRepository.findByName(request.getName()))
        .thenReturn(Optional.empty());
    Mockito.when(this.artistRepository.findByName(request.getArtistName()))
        .thenReturn(Optional.empty());

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(request);
    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void testNonExistentAlbumWithExistentArtist() {
    AddAlbumRequestBody request = new AddAlbumRequestBody("ohno", new EmptyMultipartFile(), 1L, "AmongUs");
    PostAddAlbumController testTarget = new PostAddAlbumController(this.albumRepository, this.artistRepository, this.endpoint);

    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
    ArtistEntity artistEntity = new ArtistEntity(1L, "AmongUs", Lists.newArrayList(), robotUser);
    AlbumEntity albumEntity = new AlbumEntity("ohno", artistEntity, "");

    Mockito.when(this.albumRepository.findByName(request.getName()))
        .thenReturn(Optional.empty());
    Mockito.when(this.artistRepository.findByName(request.getArtistName()))
        .thenReturn(Optional.of(artistEntity));
    Mockito.when(this.albumRepository.save(albumEntity))
        .thenReturn(albumEntity);

    ResponseEntity<MessageResponse> response = testTarget.addAlbum(request);
    assertSame(HttpStatus.OK, response.getStatusCode());
  }
}