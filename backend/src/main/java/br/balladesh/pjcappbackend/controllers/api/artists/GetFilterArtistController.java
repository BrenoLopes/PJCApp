package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.AllNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/artists")
public class GetFilterArtistController {
  private final ArtistsService artistsService;
  private final UsersService usersService;

  private final Logger logger = LoggerFactory.getLogger(GetFilterArtistController.class);

  @Autowired
  public GetFilterArtistController(ArtistsService artistsService, UsersService usersService) {
    this.artistsService = artistsService;
    this.usersService = usersService;
  }

  @GetMapping()
  public ResponseEntity<?> filterArtistBy(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String name
  ) {
    if (this.isAllOfThemNull(id, name))
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    try {
      UserEntity currentUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      ArtistEntity response;

      if (id != null)
        response = this.artistsService.searchAnArtist(id, currentUser);
      else
        response = this.artistsService.searchAnArtist(name, currentUser);

      return ResponseEntity.ok(response);
    } catch(ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);

    } catch(NotFoundException e) {
      return ResponseCreator.create(HttpStatus.NOT_FOUND);

    } catch (Exception e) {
      this.logError(e);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean isAllOfThemNull(Object... args) {
    return AllNull.withParams(args).check();
  }

  private void logError(Exception e) {
    String message = "GetFilterArtistController::filterArtistBy. Error: {}";
    logger.error(message, e.getMessage());
  }
}