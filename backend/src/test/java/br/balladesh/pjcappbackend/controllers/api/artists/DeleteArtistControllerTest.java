package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
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
class DeleteArtistControllerTest {
  @Mock
  ArtistRepository artistRepository;

  @Test
  void testDeleteNullUser() {
    long id = Defaults.getDefaultLong();

    DeleteArtistController controller = new DeleteArtistController(this.artistRepository);
    ResponseEntity<MessageResponse> response = controller.deleteArtist(id);

    if (response.getBody() == null)
      fail();

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().getMessage().length() > 1);
  }

  @Test
  void testInvalidRepository() {
    long id = 1;

    DeleteArtistController controller = new DeleteArtistController(null);
    ResponseEntity<MessageResponse> response = controller.deleteArtist(id);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertTrue(response.getBody().getMessage().length() > 1);
  }

  @Test
  void testValidRepository() {
    long id = 1;

    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
    Optional<ArtistEntity> result = Optional.of(
      new ArtistEntity("ohno", Lists.newArrayList(), robotUser)
    );
    Mockito.when(this.artistRepository.findById(id)).thenReturn(result);
    Mockito.doNothing().when(this.artistRepository).delete(result.get());

    DeleteArtistController controller = new DeleteArtistController(this.artistRepository);
    ResponseEntity<MessageResponse> response = controller.deleteArtist(id);

    assertSame(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getMessage().length() > 1);
  }
}