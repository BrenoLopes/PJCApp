package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import br.balladesh.pjcappbackend.utilities.errors.BadRequestException;
import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/albums")
public class GetFilterAlbumsController {
  private final AlbumRepository albumRepository;

  private final Logger logger = LoggerFactory.getLogger(GetFilterAlbumsController.class);

  @Autowired
  public GetFilterAlbumsController(AlbumRepository albumRepository)
  {
    this.albumRepository = albumRepository;
  }

  @GetMapping()
  public ResponseEntity<?> filterAlbumBy(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String artist,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      Page<AlbumEntity> paged = this.albumRepository.findByIdAndNameAndArtist_Name(id, name, artist, pageable);

      return ResponseEntity.ok(new PagedAlbumResponseBody(paged));
    } catch(NumberFormatException e) {
      return this.showBadRequestException(e);
    } catch(Exception e) {
      return this.showInternalServerErrorException(e);
    }
  }

  private ResponseEntity<?> showBadRequestException(Exception e) {
    this.logger.error(
        "GetFilterAlbumsController::filterAlbumBy Could not parse parameters and filter some albums: {}",
        e.getMessage()
    );

    return new CreateResponseFromExceptionFactory(
        new BadRequestException("Could not parse your request because the parameters is invalid!")
    ).create().getData();
  }

  private ResponseEntity<?> showInternalServerErrorException(Exception e) {
    this.logger.error(
        "GetFilterAlbumsController::filterAlbumBy Could not process the request and filter some albums: {}",
        e.getMessage()
    );

    return new CreateResponseFromExceptionFactory(
        new InternalServerErrorException("An error occurred in the server while trying to parse your request.")
    ).create().getData();
  }
}
