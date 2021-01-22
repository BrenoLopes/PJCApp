package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.minio.GetFromMinIOCommand;
import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;

import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class GetAllAlbumsController {
  private final AlbumRepository albumRepository;
  private final MinIOEndpoint endpoint;
  private GetFromMinIOCommand command;

  private final Logger logger = LoggerFactory.getLogger(GetAllAlbumsController.class);

  @Autowired
  public GetAllAlbumsController(AlbumRepository albumRepository, MinIOEndpoint endpoint)
  {
    this.albumRepository = albumRepository;
    this.endpoint = endpoint;
  }

  protected GetAllAlbumsController(AlbumRepository albumRepository, MinIOEndpoint endpoint, GetFromMinIOCommand command)
  {
    this.albumRepository = albumRepository;
    this.endpoint = endpoint;
    this.command = command;
  }

  @GetMapping("/list")
  public ResponseEntity<?> getAllAlbums(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pagesize,
      @RequestParam(defaultValue = "ASC") String direction
  ){
    if(HasNull.withParams(this.albumRepository, this.endpoint).check()) {
      this.logger.error("GetAllAlbumsController::getAllAlbums Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      Sort sort = Sort.by("name");

      if (!direction.equalsIgnoreCase("asc"))
        sort = sort.descending();

      Pageable _page = PageRequest.of(page, pagesize, sort);
      Page<AlbumEntity> albumPages = this.albumRepository.findAll(_page)
          .map(this::loadMinIOImages);

      return ResponseEntity.ok(new PagedAlbumResponseBody(albumPages));
    } catch(Exception e) {
      this.logger.error(
          "GetAllAlbumsController::getAllAlbums Could not process the request because of an error! Error: {}",
          e.getMessage()
      );

      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private AlbumEntity loadMinIOImages(AlbumEntity entity) {
    GetFromMinIOCommand command = this.command == null
        ? new GetFromMinIOCommand(entity.getImage(), this.endpoint)
        : this.command;

    Result<String, HttpException> result = command.execute();

    String resultData = result.haveData() ? result.getData() : "";
    entity.setImage(resultData);

    return entity;
  }
}
