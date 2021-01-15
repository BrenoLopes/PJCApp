package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.EditAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.minio.DeleteFromMinIOCommand;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.services.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.minio.UploadToMinIOCommand;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/albums", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
public class PutEditAlbumController {
  private final AlbumRepository albumRepository;
  private final ArtistRepository artistRepository;
  private final MinIOEndpoint endpoint;

  private final Logger logger = LoggerFactory.getLogger(PutEditAlbumController.class);

  @Autowired
  public PutEditAlbumController(
      AlbumRepository albumRepository,
      ArtistRepository artistRepository,
      MinIOEndpoint endpoint
  ) {
    this.albumRepository = albumRepository;
    this.artistRepository = artistRepository;
    this.endpoint = endpoint;
  }

  @PutMapping
  public ResponseEntity<MessageResponse> editAlbum(EditAlbumRequestBody data) {
    // Check if the album exists
    Optional<AlbumEntity> _album = this.albumRepository.findById(data.getAlbumId());
    if (!_album.isPresent()) {
      String message = "Album not found!";
      return new CreateResponseFromExceptionFactory(new NotFoundException(message))
          .create().getData();
    }

    // Unbox the entity
    AlbumEntity album = _album.get();

    try {
      this.updateAlbumArtist(data, album);
      this.updateAlbumName(data, album);
      this.updateAlbumImage(data, album);

      this.albumRepository.save(album);

      return ResponseEntity.ok(
          new MessageResponse("The album was updated successfully.")
      );
    } catch (HttpException e) {
      return new CreateResponseFromExceptionFactory(e)
          .create().getData();
    } catch (Exception e) {
      return new CreateResponseFromExceptionFactory(
          new InternalServerErrorException()
      ).create().getData();
    }
  }

  private void updateAlbumName(EditAlbumRequestBody data, AlbumEntity entity) {
    if (data.getName().equals(""))
      return;

    entity.setName(data.getName());
  }

  private void updateAlbumImage(EditAlbumRequestBody data, AlbumEntity entity) throws HttpException {
    if (data.getImage().isEmpty())
      return;

    // Upload the new image
    UploadToMinIOCommand uploader = new UploadToMinIOCommand(data.getImage(), this.endpoint);
    Result<String, HttpException> result = uploader.execute();

    if (!result.haveData()) {
      String message = "PutEditAlbumController::updateAlbumImage Failed to upload image to MinIO server! Error {}";
      this.logger.error(message, result.getException().getMessage());
      throw new InternalServerErrorException();
    }

    // Clean the older image if it isn't empty.
    if (!entity.getImage().equals("")) {
      DeleteFromMinIOCommand deleter = new DeleteFromMinIOCommand(entity.getImage(), this.endpoint);
      deleter.execute();
    }

    entity.setImage(result.getData());
  }

  private void updateAlbumArtist(EditAlbumRequestBody data, AlbumEntity entity) throws HttpException {
    if (data.getArtistId() == Long.MIN_VALUE)
      return;

    // Check if the artist exist
    Optional<ArtistEntity> _artist =  this.artistRepository.findById(data.getArtistId());
    if (!_artist.isPresent())
      throw new BadRequestException("This artist does not exist!");

    entity.setArtist(_artist.get());
  }
}