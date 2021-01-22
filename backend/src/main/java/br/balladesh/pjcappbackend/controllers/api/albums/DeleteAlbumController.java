package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.minio.DeleteFromMinIOCommand;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
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
  private final AlbumRepository albumRepository;
  private final MinIOEndpoint endpoint;

  private final Logger logger = LoggerFactory.getLogger(DeleteAlbumController.class);

  @Autowired
  public DeleteAlbumController(AlbumRepository albumRepository, MinIOEndpoint endpoint) {
    this.albumRepository = albumRepository;
    this.endpoint = endpoint;
  }

  @DeleteMapping
  public ResponseEntity<MessageResponse> deleteAlbum(@RequestParam Optional<Long> id) {
    if(HasNull.withParams(this.albumRepository, this.endpoint).check()) {
      this.logger.error("DeleteAlbumController::deleteAlbum Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    HttpStatus status;

    try {
      long _id = id.orElseThrow(BadRequestException::new);

      AlbumEntity album = this.albumRepository.findById(_id)
          .orElseThrow(NotFoundException::new);

      if (!album.getImage().equals("")) {
        DeleteFromMinIOCommand deleter = new DeleteFromMinIOCommand(album.getImage(), this.endpoint);
        deleter.execute();
      }

      this.albumRepository.delete(album);

      status = HttpStatus.OK;
    } catch (BadRequestException e) {
      status = HttpStatus.BAD_REQUEST;
    } catch (NotFoundException e) {
      status = HttpStatus.NOT_FOUND;
    } catch (Exception e) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return ResponseCreator.create(status);
  }
}
