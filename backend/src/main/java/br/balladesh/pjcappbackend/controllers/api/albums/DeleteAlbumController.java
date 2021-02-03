package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class DeleteAlbumController {
  private final AlbumsService albumsService;
  private final UsersService usersService;

  private final Logger logger = LoggerFactory.getLogger(DeleteAlbumController.class);

  @Autowired
  public DeleteAlbumController(AlbumsService albumsService, UsersService usersService) {
    this.usersService = usersService;
    this.albumsService = albumsService;
  }

  @DeleteMapping
  public ResponseEntity<MessageResponse> deleteAlbum(@RequestParam(name = "id") Optional<Long> requestId) {
    try {
      long theId = requestId.orElseThrow(BadRequestException::new);

      UserEntity currentUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      this.albumsService.removeAnAlbum(theId, currentUser);

      return ResponseCreator.create(HttpStatus.OK);
    } catch (ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);
    } catch (BadRequestException e) {
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      return ResponseCreator.create(HttpStatus.NOT_FOUND);
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
