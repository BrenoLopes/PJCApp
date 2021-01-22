package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.AddAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.minio.UploadToMinIOCommand;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;

import br.balladesh.pjcappbackend.utilities.factories.ResponseCreator;
import br.balladesh.pjcappbackend.utilities.predicates.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/albums", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
public class PostAddAlbumController {
  private final AlbumRepository albumRepository;
  private final ArtistRepository artistRepository;
  private final MinIOEndpoint endpoint;

  private final Logger logger = LoggerFactory.getLogger(PostAddAlbumController.class);

  @Autowired
  public PostAddAlbumController(
      AlbumRepository albumRepository,
      ArtistRepository artistRepository,
      MinIOEndpoint endpoint
  )
  {
    this.albumRepository = albumRepository;
    this.artistRepository = artistRepository;
    this.endpoint = endpoint;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> addAlbum(AddAlbumRequestBody data) {
    if(NonNull.withParams(this.albumRepository, this.artistRepository, this.endpoint).check()){
      this.logger.error("PostAddAlbumController::addAlbum Required constructors was not autowired.");
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Check if this album already exists in the database
    if (this.albumRepository.findByName(data.getName()).isPresent())
      return ResponseCreator.create(HttpStatus.CONFLICT);

    // Check if the artist exists. This method should not create one.
    Optional<ArtistEntity> _artist = this.findArtist(data);
    if (!_artist.isPresent()) {
      String message = "An artist_id or artist_name must exist and not be empty!";
      return ResponseCreator.create(message, HttpStatus.BAD_REQUEST);
    }

    // Prepare for upload
    UploadToMinIOCommand uploader = new UploadToMinIOCommand(data.getImage(), this.endpoint);
    String image = uploader.getFileName();

    try {
      ArtistEntity artist = _artist.get();
      AlbumEntity album = new AlbumEntity(data.getName(), artist, image);
      this.albumRepository.save(album);

      // If there is no image to upload, then...
      if (data.getImage().isEmpty())
        return ResponseCreator.create(HttpStatus.OK);

      // Else upload it
      Result<String, HttpException> result = uploader.execute();

      if (result.haveData())
        return ResponseCreator.create(HttpStatus.OK);

      // Failed to upload
      this.albumRepository.delete(album);
      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      String message = "PostAddAlbumController::addAlbum Failed to save the data in the database. Error: {}";
      logger.error(message,e.getMessage());

      return ResponseCreator.create(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Optional<ArtistEntity> findArtist(AddAlbumRequestBody data) {
    if (!data.getArtistName().equals(Defaults.DEFAULT_STR)) {
      String name = data.getArtistName();
      return this.artistRepository.findByName(name);
    }

    if (data.getArtistId() != Defaults.getDefaultLong()) {
      long id = data.getArtistId();
      return this.artistRepository.findById(id);
    }

    return Optional.empty();
  }
}
