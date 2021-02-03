package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;

import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class GetAllAlbumsController {
  private final AlbumsService albumsService;
  private final UsersService usersService;

  private final Logger logger = LoggerFactory.getLogger(GetAllAlbumsController.class);

  @Autowired
  public GetAllAlbumsController(AlbumsService albumsService, UsersService usersService)
  {
    this.albumsService = albumsService;
    this.usersService = usersService;
  }

  @GetMapping("/list")
  public ResponseEntity<?> getAllAlbums(
      @RequestParam(name = "artistid") long artistId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pagesize,
      @RequestParam(defaultValue = "ASC") String direction
  ){
    try {
      UserEntity currentUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      Page<AlbumEntity> response = this.albumsService.getAllAlbumsFromArtist(
          artistId,
          currentUser,
          page,
          pagesize,
          direction
      );

      return ResponseEntity.ok(new PagedAlbumResponseBody(response));
    } catch (ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      this.logError(e);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void logError(Exception e) {
    String message = "DeleteAlbumController::deleteAlbum. Error: {}";
    logger.error(message, e.getMessage());
  }
}
