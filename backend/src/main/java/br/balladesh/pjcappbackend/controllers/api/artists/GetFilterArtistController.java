package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.dto.api.artists.PagedArtistResponseBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    if(NonNull.withParams(this.artistRepository).check()){
      this.logger.error("GetFilterArtistController::filterArtistBy Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      Sort sort = Sort.by("name");
      if (direction.equalsIgnoreCase("desc"))
        sort = sort.descending();

      Pageable pageable = PageRequest.of(page, size, sort);
      Page<ArtistEntity> paged = this.artistRepository.findByIdAndName(id, name, pageable);

      return ResponseEntity.ok(new PagedArtistResponseBody(paged));
    } catch(NumberFormatException e) {
      this.logger.error(
          "GetFilterArtistController::filterArtistBy Could not parse the parameters and filter the list of artists: {}",
          e.getMessage()
      );

      return ResponseCreator.create(HttpStatus.BAD_REQUEST);
    } catch(Exception e) {
      this.logger.error(
          "GetFilterArtistController::filterArtistBy Could not process the request and filter the list of artists: {}",
          e.getMessage()
      );

      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
