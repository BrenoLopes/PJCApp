package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.artists.AddArtistRequestDTO;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    if (name == null)
      return new CreateResponseFromExceptionFactory(
          new BadRequestException("The artist's name cannot be empty!")
      ).create().getData();

    if (this.artistRepository.findByName(name.getName()).isPresent())
      return new CreateResponseFromExceptionFactory(
          new ConflictException("An artist with this name already exists!")
      ).create().getData();

    try {
      ArtistEntity artist = new ArtistEntity();
      artist.setName(name.getName());

      this.artistRepository.save(artist);

      return ResponseEntity.ok(new MessageResponse("The request was processed successfully!"));
    } catch (Exception e) {
      logger.error("PostAddArtistController::addArtist Failed to persist the artist. Error: {}", e.getMessage());

      return new CreateResponseFromExceptionFactory(
          new InternalServerErrorException()
      ).create().getData();
    }
  }
}
