package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.artists.PutArtistRequestDTO;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/artists")
public class PutEditArtistController {
  private final ArtistRepository artistRepository;
  private final Logger logger = LoggerFactory.getLogger(PutEditArtistController.class);

  public PutEditArtistController(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  @PutMapping
  public ResponseEntity<MessageResponse> editArtist(@RequestBody PutArtistRequestDTO request) {
    if(HasNull.withParams(this.artistRepository).check()){
      this.logger.error("PutEditArtistController::editArtist Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      Optional<ArtistEntity> _artist = this.artistRepository.findById(request.getId());
      if (!_artist.isPresent())
        return ResponseCreator.create(HttpStatus.NOT_FOUND);

      ArtistEntity artist = _artist.get();
      artist.setName(request.getName());

      this.artistRepository.save(artist);

      return ResponseCreator.create(HttpStatus.OK);
    } catch (Exception e) {
      logger.error("PutEditArtistController::editArtist Failed to edit an artist!");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
