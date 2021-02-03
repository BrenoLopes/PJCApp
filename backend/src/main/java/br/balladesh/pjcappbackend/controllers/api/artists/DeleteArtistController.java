package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/artists")
public class DeleteArtistController {
  private final ArtistsService artistsService;
  private final UsersService usersService;

  private final Logger logger = LoggerFactory.getLogger(DeleteArtistController.class);

  @Autowired
  public DeleteArtistController(ArtistsService artistsService, UsersService usersService) {
    this.artistsService = artistsService;
    this.usersService = usersService;
  }

  @DeleteMapping
  public ResponseEntity<MessageResponse> deleteArtist(Optional<Long> id) {
    try {
      long theId = id.orElseThrow(BadRequestException::new);

      UserEntity thisUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      this.artistsService.removeAnArtist(theId, thisUser);

      return ResponseCreator.create(HttpStatus.OK);

    } catch(ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);

    } catch(BadRequestException e) {
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    } catch (Exception e) {
      this.logError(e);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void logError(Exception e) {
    String message = "DeleteArtistController::deleteArtist failed to delete the user from the repo. Error: {}";
    logger.error(message, e.getMessage());
  }
}
