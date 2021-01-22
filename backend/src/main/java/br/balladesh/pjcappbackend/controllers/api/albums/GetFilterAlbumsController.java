package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.minio.GetFromMinIOCommand;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
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
@RequestMapping("/api/albums")
public class GetFilterAlbumsController {
  private final AlbumRepository albumRepository;
  private final MinIOEndpoint endpoint;

  private final Logger logger = LoggerFactory.getLogger(GetFilterAlbumsController.class);

  @Autowired
  public GetFilterAlbumsController(AlbumRepository albumRepository, MinIOEndpoint endpoint)
  {
    this.albumRepository = albumRepository;
    this.endpoint = endpoint;
  }

  @GetMapping()
  public ResponseEntity<?> filterAlbumBy(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String artist,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "ASC") String direction
  ) {
    if(NonNull.withParams(this.albumRepository, this.endpoint).check()) {
      this.logger.error("GetFilterAlbumsController::filterAlbumBy Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      Sort sort = Sort.by("name");
      if (!direction.equalsIgnoreCase("asc"))
        sort = sort.descending();

      Pageable pageable = PageRequest.of(page, size, sort);
      Page<AlbumEntity> paged = this.albumRepository.findByIdAndNameAndArtist_Name(id, name, artist, pageable)
          .map(this::loadMinIOImages);

      return ResponseEntity.ok(new PagedAlbumResponseBody(paged));
    } catch(NumberFormatException e) {
      return this.showBadRequestException(e);
    } catch(Exception e) {
      return this.showInternalServerErrorException(e);
    }
  }

  private AlbumEntity loadMinIOImages(AlbumEntity entity) {
    Result<String, HttpException> result = new GetFromMinIOCommand(entity.getImage(), this.endpoint)
        .execute();

    String resultData = result.haveData() ? result.getData() : "";
    entity.setImage(resultData);

    return entity;
  }

  private ResponseEntity<?> showBadRequestException(Exception e) {
    this.logger.error(
        "GetFilterAlbumsController::filterAlbumBy Could not parse parameters and filter some albums: {}",
        e.getMessage()
    );

    return ResponseCreator.create(HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<?> showInternalServerErrorException(Exception e) {
    this.logger.error(
        "GetFilterAlbumsController::filterAlbumBy Could not process the request and filter some albums: {}",
        e.getMessage()
    );

    return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
