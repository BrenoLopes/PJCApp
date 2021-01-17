package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.artists.PutArtistRequestDTO;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    if (request == null)
      return new CreateResponseFromExceptionFactory(
          new BadRequestException("Nothing do to.")
      ).create().getData();

    try {
      Optional<ArtistEntity> _artist = this.artistRepository.findById(request.getId());
      if (!_artist.isPresent())
        return new CreateResponseFromExceptionFactory(new NotFoundException()).create().getData();

      ArtistEntity artist = _artist.get();
      artist.setName(request.getName());

      this.artistRepository.save(artist);

      return ResponseEntity.ok(new MessageResponse("The artist was updated successfully!"));
    } catch (Exception e) {
      logger.error("PutEditArtistController::editArtist Failed to edit an artist!");

      return new CreateResponseFromExceptionFactory(
          new InternalServerErrorException()
      ).create().getData();
    }
  }
}
