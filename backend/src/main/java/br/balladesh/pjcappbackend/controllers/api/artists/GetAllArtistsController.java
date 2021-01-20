package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.api.artists.PagedArtistResponseBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/artists")
public class GetAllArtistsController {
  private final ArtistRepository artistRepository;
  private final Logger logger = LoggerFactory.getLogger(GetAllArtistsController.class);

  public GetAllArtistsController(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  @GetMapping("/list")
  public ResponseEntity<?> getAllArtists(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pagesize,
      @RequestParam(defaultValue = "ASC") String direction
  ) {
    if(NonNull.withParams(this.artistRepository).check()){
      this.logger.error("GetAllArtistsController::getAllArtists Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      Sort sort = Sort.by("name");

      if (!direction.equalsIgnoreCase("asc")) {
        sort = sort.descending();
      }

      Pageable _page = PageRequest.of(page, pagesize, sort);
      Page<ArtistEntity> artistPages = this.artistRepository.findAll(_page);

      return ResponseEntity.ok(new PagedArtistResponseBody(artistPages));
    } catch(Exception e) {
      this.logger.error(
          "GetAllArtistsController::getAllArtists Could not process the request because of an error! Error: {}",
          e.getMessage()
      );

      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
