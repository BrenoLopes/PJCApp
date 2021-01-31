package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.artists.AddArtistRequestDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PostAddArtistControllerTest {
  @Mock
  private ArtistRepository artistRepository;

  UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");

  @Test
  void testNullRepository() {
    AddArtistRequestDTO request = new AddArtistRequestDTO("ohno");
    PostAddArtistController testTarget = new PostAddArtistController(null);
    ResponseEntity<?> result = testTarget.addArtist(request);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
  }

  @Test
  void testWithoutName() {
    PostAddArtistController testTarget = new PostAddArtistController(this.artistRepository);
    ResponseEntity<MessageResponse> result = testTarget.addArtist(null);

    assertSame(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void testWithSameName() {
    AddArtistRequestDTO request = new AddArtistRequestDTO("ohno");
    PostAddArtistController testTarget = new PostAddArtistController(this.artistRepository);

    Mockito.when(this.artistRepository.findByName(request.getName()))
        .thenReturn(Optional.of(new ArtistEntity("ohno", Lists.newArrayList(), robotUser)));

    ResponseEntity<MessageResponse> result = testTarget.addArtist(request);

    assertSame(HttpStatus.CONFLICT, result.getStatusCode());
  }

  @Test
  void testWithValidName() {
    ArtistEntity entity = new ArtistEntity("ohno", Lists.newArrayList(), robotUser);

    AddArtistRequestDTO request = new AddArtistRequestDTO("ohno");
    PostAddArtistController testTarget = new PostAddArtistController(this.artistRepository);

    Mockito.when(this.artistRepository.findByName(request.getName()))
        .thenReturn(Optional.empty());
    Mockito.when(this.artistRepository.save(entity)).thenReturn(entity);

    ResponseEntity<MessageResponse> result = testTarget.addArtist(request);
    assertSame(HttpStatus.OK, result.getStatusCode());
  }
}