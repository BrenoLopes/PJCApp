package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.AddAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.AlbumsService;
import br.balladesh.pjcappbackend.services.ArtistsService;
import br.balladesh.pjcappbackend.services.UsersService;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/albums", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
public class PostAddAlbumController {
  private final AlbumsService albumsService;
  private final UsersService usersService;
  private final ArtistsService artistsService;

  private final Logger logger = LoggerFactory.getLogger(PostAddAlbumController.class);

  @Autowired
  public PostAddAlbumController(AlbumsService albumsService, UsersService usersService, ArtistsService artistsService) {
    this.albumsService = albumsService;
    this.usersService = usersService;
    this.artistsService = artistsService;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> addAlbum(AddAlbumRequestBody data) {
    if (data == null)
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    try {
      UserEntity currentUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      ArtistEntity artist = this.artistsService.searchAnArtist(data.getArtistId(), currentUser);

      if (!artist.getOwner().equals(currentUser))
        throw new ForbiddenException("You don't have permissions to use this artist!");

      if (!this.albumsService.addAnAlbum(artist, currentUser, data.getName(), data.getImage()))
        throw new InternalServerErrorException("Failed to save the data into the database. Data: " + data.toString());

      return ResponseCreator.create(HttpStatus.OK);
    } catch (ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);
    } catch (ConflictException e) {
      return ResponseCreator.create(HttpStatus.CONFLICT);
    } catch (NotFoundException e) {
      return ResponseCreator.create("This artist doesn't exist.", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      this.logError(e);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void logError(Exception e) {
    String message = "PostAddAlbumController::addAlbum. Error: {}";
    logger.error(message, e.getMessage());
  }
}
