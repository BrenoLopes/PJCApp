package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.artists.AddArtistRequestDTO;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/artists")
public class PostAddArtistController {
  private final ArtistRepository artistRepository;
  private final Logger logger = LoggerFactory.getLogger(PostAddArtistController.class);

  public PostAddArtistController(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> addArtist(@RequestBody AddArtistRequestDTO name) {
    if(NonNull.withParams(this.artistRepository).check()){
      this.logger.error("PostAddArtistController::addArtist Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    if(name == null)
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    if (this.artistRepository.findByName(name.getName()).isPresent())
      return ResponseCreator.create(HttpStatus.CONFLICT);

    try {
      ArtistEntity artist = new ArtistEntity();
      artist.setName(name.getName());

      this.artistRepository.save(artist);

      return ResponseCreator.create(HttpStatus.OK);
    } catch (Exception e) {
      logger.error("PostAddArtistController::addArtist Failed to persist the artist. Error: {}", e.getMessage());

      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
