package br.balladesh.pjcappbackend.controllers.api.artists;

import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.artists.PutArtistRequestDTO;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/artists")
public class PutEditArtistController {
  private final ArtistsService artistsService;
  private final UsersService usersService;

  private final Logger logger = LoggerFactory.getLogger(PutEditArtistController.class);

  @Autowired
  public PutEditArtistController(ArtistsService artistsService, UsersService usersService) {
    this.artistsService = artistsService;
    this.usersService = usersService;
  }

  @PutMapping
  public ResponseEntity<MessageResponse> editArtist(@RequestBody PutArtistRequestDTO request) {
    if (request == null)
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    try {
      final UserEntity currentUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      boolean response = this.artistsService.setAnArtist(
          request.getId(),
          Optional.ofNullable(request.getName()),
          Optional.empty(),
          currentUser
      );

      if (!response)
        throw new InternalServerErrorException("Failed to save this artist into the database");

      return ResponseCreator.create(HttpStatus.OK);
    } catch (ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      this.logError(e);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void logError(Exception e) {
    String message = "PostAddArtistController::addArtist. Error: {}";
    logger.error(message, e.getMessage());
  }
}
