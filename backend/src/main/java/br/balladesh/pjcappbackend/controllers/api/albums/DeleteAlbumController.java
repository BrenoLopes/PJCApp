package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.minio.DeleteFromMinIOCommand;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.services.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    try {
      AlbumEntity album = this.findAlbum(this.getId(id));

      if (!album.getImage().equals("")) {
        DeleteFromMinIOCommand deleter = new DeleteFromMinIOCommand(album.getImage(), this.endpoint);
        deleter.execute();
      }

      this.albumRepository.delete(album);

      return ResponseEntity.ok(
          new MessageResponse("The album was deleted successfully.")
      );
    } catch (BadRequestException e) {
      return this.buildResponse(new BadRequestException("The id was not set!"));
    } catch (NotFoundException e) {
      return this.buildResponse(new NotFoundException("This album doesn't exist!"));
    } catch (Exception e) {
      return this.buildResponse(new InternalServerErrorException());
    }
  }

  private long getId(Optional<Long> id) throws BadRequestException {
    return id.orElseThrow(BadRequestException::new);
  }

  private AlbumEntity findAlbum(long id) throws NotFoundException {
    return this.albumRepository.findById(id)
        .orElseThrow(NotFoundException::new);
  }
  private ResponseEntity<MessageResponse> buildResponse(HttpException e) {
    return new CreateResponseFromExceptionFactory(e).create().getData();
  }
}
