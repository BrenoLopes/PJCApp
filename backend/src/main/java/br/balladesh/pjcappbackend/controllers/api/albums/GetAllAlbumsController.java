package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.minio.GetFromMinIOCommand;
import br.balladesh.pjcappbackend.services.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.function.Function;

@RestController
@RequestMapping("/api/albums")
public class GetAllAlbumsController {
  private final AlbumRepository albumRepository;
  private final MinIOEndpoint endpoint;

  private final Logger logger = LoggerFactory.getLogger(GetAllAlbumsController.class);

  @Autowired
  public GetAllAlbumsController(AlbumRepository albumRepository, MinIOEndpoint endpoint)
  {
    this.albumRepository = albumRepository;
    this.endpoint = endpoint;
  }

  @GetMapping("/list")
  public ResponseEntity<?> getAllAlbums(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pagesize,
      @RequestParam(defaultValue = "ASC") String direction
  ){
    try {
      Sort sort = Sort.by("name");

      if (!direction.equalsIgnoreCase("asc")) {
        sort = sort.descending();
      }

      Pageable _page = PageRequest.of(page, pagesize, sort);
      Page<AlbumEntity> albumPages = this.albumRepository.findAll(_page)
          .map(this::loadMinIOImages);

      return ResponseEntity.ok(new PagedAlbumResponseBody(albumPages));
    } catch(Exception e) {
      this.logger.error(
          "GetAllAlbumsController::getAllAlbums Could not process the request because of an error! Error: {}",
          e.getMessage()
      );

      return new CreateResponseFromExceptionFactory(
          new InternalServerErrorException("An error happened in the server! Please try again latter!")
      ).create().getData();
    }
  }

  private AlbumEntity loadMinIOImages(AlbumEntity entity) {
    Result<String, HttpException> result = new GetFromMinIOCommand(entity.getImage(), this.endpoint)
        .execute();

    String resultData = result.haveData() ? result.getData() : "";
    entity.setImage(resultData);

    return entity;
  }
}
