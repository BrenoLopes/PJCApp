package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    if(NonNull.withParams(this.artistRepository).check()){
      this.logger.error("DeleteArtistController::deleteArtist Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    if (id == Defaults.getDefaultLong())
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    try {
      Optional<ArtistEntity> _artist = this.artistRepository.findById(id);
      if (!_artist.isPresent())
        return ResponseCreator.create(HttpStatus.NOT_FOUND);

      this.artistRepository.delete(_artist.get());

      return ResponseCreator.create(HttpStatus.OK);
    } catch (Exception e) {
      String message = "DeleteArtistController::deleteArtist failed to delete the user from the repo. Error: {}";
      logger.error(message, e.getMessage());
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
