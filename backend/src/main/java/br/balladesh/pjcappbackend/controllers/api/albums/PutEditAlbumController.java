package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.ForbiddenException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.EditAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/albums", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
public class PutEditAlbumController {
  private final AlbumsService albumsService;
  private final UsersService usersService;
  private final ArtistsService artistsService;

  private final Logger logger = LoggerFactory.getLogger(PutEditAlbumController.class);

  @Autowired
  public PutEditAlbumController(AlbumsService albumsService, UsersService usersService, ArtistsService artistsService) {
    this.albumsService = albumsService;
    this.usersService = usersService;
    this.artistsService = artistsService;
  }

  @PutMapping
  public ResponseEntity<MessageResponse> editAlbum(EditAlbumRequestBody data) {
    if (data == null)
      return ResponseCreator.create(HttpStatus.BAD_REQUEST);

    if (data.getName() == null && data.getImage().isEmpty())
      return ResponseCreator.create(HttpStatus.OK);

    try {
      UserEntity currentUser = this.usersService.getCurrentAuthenticatedUser()
          .orElseThrow(ForbiddenException::new);

      AlbumEntity theAlbum = this.albumsService.searchAnAlbumWithoutMinIO(data.getAlbumId(), currentUser);

      if (!theAlbum.getArtist().getOwner().equals(currentUser))
        throw new ForbiddenException("You don't have permissions to use this artist!");

      String newName = null;
      MultipartFile newImage = null;

      if (data.getName() != null && !data.getName().equals(""))
        newName = data.getName();

      if (data.getImage() != null && !data.getImage().isEmpty())
        newImage = data.getImage();

      this.albumsService.setAnAlbum(data.getAlbumId(), currentUser, newName, newImage);

      return ResponseCreator.create(HttpStatus.OK);
    } catch (NotFoundException e) {
      return ResponseCreator.create(HttpStatus.NOT_FOUND);
    } catch (ForbiddenException e) {
      return ResponseCreator.create(HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      this.logError(e);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void logError(Exception e) {
    String message = "PutEditAlbumController::editAlbum. Error: {}";
    logger.error(message, e.getMessage());
  }
}