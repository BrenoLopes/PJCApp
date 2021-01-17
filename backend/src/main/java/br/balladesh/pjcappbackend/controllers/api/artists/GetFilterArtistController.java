package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.api.artists.PagedArtistResponseBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/artists")
public class GetFilterArtistController {
  private final ArtistRepository artistRepository;

  private final Logger logger = LoggerFactory.getLogger(GetFilterArtistController.class);

  @Autowired
  public GetFilterArtistController(ArtistRepository artistRepository)
  {
    this.artistRepository = artistRepository;
  }

  @GetMapping()
  public ResponseEntity<?> filterArtistBy(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "ASC") String direction
  ) {
    try {
      Sort sort = Sort.by("name");
      if (direction.equalsIgnoreCase("desc"))
        sort = sort.descending();

      Pageable pageable = PageRequest.of(page, size, sort);
      Page<ArtistEntity> paged = this.artistRepository.findByIdAndName(id, name, pageable);

      return ResponseEntity.ok(new PagedArtistResponseBody(paged));
    } catch(NumberFormatException e) {
      return this.showBadRequestException(e);
    } catch(Exception e) {
      return this.showInternalServerErrorException(e);
    }
  }

  private ResponseEntity<?> showBadRequestException(Exception e) {
    this.logger.error(
        "GetFilterArtistController::filterArtistBy Could not parse the parameters and filter the list of artists: {}",
        e.getMessage()
    );

    return new CreateResponseFromExceptionFactory(
        new BadRequestException("Could not parse your request because the parameters is invalid!")
    ).create().getData();
  }

  private ResponseEntity<?> showInternalServerErrorException(Exception e) {
    this.logger.error(
        "GetFilterArtistController::filterArtistBy Could not process the request and filter the list of artists: {}",
        e.getMessage()
    );

    return new CreateResponseFromExceptionFactory(
        new InternalServerErrorException("An error occurred in the server while trying to parse your request.")
    ).create().getData();
  }
}
