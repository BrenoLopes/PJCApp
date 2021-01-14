package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.utilities.builders.BuildResponseFromException;
import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponse;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class GetAllAlbumsController {
  private final AlbumRepository albumRepository;

  private final Logger logger = LoggerFactory.getLogger(GetAllAlbumsController.class);

  @Autowired
  public GetAllAlbumsController(AlbumRepository albumRepository)
  {
    this.albumRepository = albumRepository;
  }

  @GetMapping("/list")
  public ResponseEntity<?> getAllAlbums(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pagesize
  ){
    try {
      Pageable _page = PageRequest.of(page, pagesize);
      Page<AlbumEntity> albumPages = this.albumRepository.findAll(_page);

      return ResponseEntity.ok(new PagedAlbumResponse(albumPages));
    } catch(Exception e) {
      this.logger.error(
          "GetAllAlbumsController::getAllAlbums Could not process the request because of an error! Error: {}",
          e.getMessage()
      );

      return new BuildResponseFromException(
          new InternalServerErrorException("An error happened in the server! Please try again latter!")
      ).build().getData();
    }
  }
}
