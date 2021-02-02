package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class GetFilterAlbumsController {
  private final AlbumsService albumsService;
  private final UsersService usersService;

  private final Logger logger = LoggerFactory.getLogger(GetFilterAlbumsController.class);

  @Autowired
  public GetFilterAlbumsController(AlbumsService albumsService, UsersService usersService)
  {
    this.albumsService = albumsService;
    this.usersService = usersService;
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
    try {
      UserEntity currentUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      Page<AlbumEntity> result = this.albumsService.searchAnAlbumByMultipleParameters(
          currentUser,
          page,
          size,
          direction,
          Optional.ofNullable(id),
          Optional.ofNullable(name),
          Optional.ofNullable(artist)
      );

      return ResponseEntity.ok(new PagedAlbumResponseBody(result));
    } catch (ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      this.logError(e);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void logError(Exception e) {
    String message = "GetFilterAlbumsController::filterAlbumBy. Error: {}";
    logger.error(message, e.getMessage());
  }
}
