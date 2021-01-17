package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.Defaults;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/artists")
public class DeleteArtistController {
  private final ArtistRepository artistRepository;

  private final Logger logger = LoggerFactory.getLogger(DeleteArtistController.class);

  public DeleteArtistController(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  @DeleteMapping
  public ResponseEntity<MessageResponse> deleteArtist(@RequestParam(defaultValue = Defaults.DEFAULT_LONG) long id) {
    if (id == Long.parseLong(Defaults.DEFAULT_LONG)) {
      return new CreateResponseFromExceptionFactory(new BadRequestException()).create().getData();
    }

    try {
      Optional<ArtistEntity> _artist = this.artistRepository.findById(id);
      if (!_artist.isPresent()) {
        return new CreateResponseFromExceptionFactory(new NotFoundException()).create().getData();
      }

      this.artistRepository.delete(_artist.get());

      return ResponseEntity.ok(new MessageResponse("The artist was deleted successfully"));
    } catch (Exception e) {
      String message = "DeleteArtistController::deleteArtist failed to delete the user from the repo. Error: {}";
      logger.error(message, e.getMessage());

      return new CreateResponseFromExceptionFactory(new InternalServerErrorException()).create().getData();
    }
  }
}
