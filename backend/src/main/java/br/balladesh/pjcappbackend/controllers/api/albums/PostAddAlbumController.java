package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.AddAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.services.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.minio.UploadToMinIOCommand;
import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<?> addAlbum(AddAlbumRequestBody data) {
    // Check if this album already exists in the database
    if (this.albumRepository.findByName(data.getName()).isPresent()) {
      return new CreateResponseFromExceptionFactory(new ConflictException("An album with this name already exists!"))
          .create().getData();
    }

    // Check if the artist exists. This method should not create one.
    Optional<ArtistEntity> _artist = this.createArtist(data);
    if (!_artist.isPresent()) {
      String message = "An artist_id or artist_name must be not be empty!";
      return new CreateResponseFromExceptionFactory(new BadRequestException(message))
          .create().getData();
    }

    UploadToMinIOCommand uploader = new UploadToMinIOCommand(data.getImage(), this.endpoint);
    String image = uploader.getFileName();

    try {
      ArtistEntity artist = _artist.get();
      AlbumEntity album = new AlbumEntity(data.getName(), artist, image);
      this.albumRepository.save(album);

      // If there is no image to upload, then...
      if (data.getImage().isEmpty())
        return this.createSuccessfulResponse();

      // Else upload it
      Result<String, HttpException> result = uploader.execute();

      if (result.haveData())
        return this.createSuccessfulResponse();

      // Failed to upload
      this.albumRepository.delete(album);
      return this.createInternalServerErrorResponse();

    } catch (Exception e) {
      String message = "PostAddAlbumController::addAlbum Failed to save the data in the database. Error: {}";
      logger.error(message,e.getMessage());
      return this.createInternalServerErrorResponse();
    }
  }

  private Optional<ArtistEntity> createArtist(AddAlbumRequestBody data) {
    if (!data.getArtistName().equals("")) {
      String name = data.getArtistName();
      return this.artistRepository.findByName(name);
    }

    if (data.getArtistId() != Long.MIN_VALUE) {
      long id = data.getArtistId();
      return this.artistRepository.findById(id);
    }

    return Optional.empty();
  }

  private ResponseEntity<MessageResponse> createInternalServerErrorResponse() {
    String message = "An error happened in the server while trying to process your request.";

    return new CreateResponseFromExceptionFactory(new InternalServerErrorException(message))
        .create().getData();
  }

  private ResponseEntity<MessageResponse> createSuccessfulResponse() {
    return ResponseEntity.ok(new MessageResponse("The album was inserted successfully."));
  }
}
