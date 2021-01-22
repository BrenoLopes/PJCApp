package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.EditAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.minio.DeleteFromMinIOCommand;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.minio.UploadToMinIOCommand;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    if(HasNull.withParams(this.albumRepository, this.artistRepository, this.endpoint).check()){
      this.logger.error("PutEditAlbumController::editAlbum Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Check if the album exists
    Optional<AlbumEntity> _album = this.albumRepository.findById(data.getAlbumId());
    if (!_album.isPresent())
      return ResponseCreator.create("There is no album found with this id.", HttpStatus.NOT_FOUND);

    AlbumEntity album = _album.get();

    try {
      this.updateAlbumArtist(data, album);
      this.updateAlbumName(data, album);
      this.updateAlbumImage(data, album);

      this.albumRepository.save(album);

      return ResponseCreator.create(HttpStatus.OK);
    } catch (HttpException e) {
      return ResponseCreator.create(e.getStatusCode());
    } catch (Exception e) {
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void updateAlbumName(EditAlbumRequestBody data, AlbumEntity entity) {
    if (data.getName().equals(Defaults.DEFAULT_STR))
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
    if (data.getArtistId() == Defaults.getDefaultLong())
      return;

    // Check if the artist exist
    Optional<ArtistEntity> _artist =  this.artistRepository.findById(data.getArtistId());
    if (!_artist.isPresent())
      throw new BadRequestException("This artist does not exist!");

    entity.setArtist(_artist.get());
  }
}