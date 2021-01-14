package br.balladesh.pjcappbackend.controllers.api.albums;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.api.albums.AddAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.services.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.ConflictException;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;

import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import br.balladesh.pjcappbackend.utilities.factories.AlbumFactory;
import br.balladesh.pjcappbackend.utilities.factories.CreateResponseFromExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/albums", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
public class PostAddAlbumController {
  private final AlbumRepository albumRepository;
  private final ArtistRepository artistRepository;
  private final MinIOEndpoint endpoint;

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
    // If the album already exists, abort.
    if (this.albumRepository.findByName(data.getName()).isPresent()) {
      return new CreateResponseFromExceptionFactory(
          new ConflictException("An album already exists with that name!")
      ).create().getData();
    }

    try {
      Result<ArtistEntity, HttpException> result = AlbumFactory.from(data, artistRepository, endpoint)
          .create();

      // Log the error and create an appropriate view.
      if (!result.haveData())
        return new CreateResponseFromExceptionFactory(result.getException()).create().getData();

      // Persist it into the database if everything worked fine
      this.artistRepository.save(result.getData());

      // TODO Add image to MINIO and fix bugs

      return ResponseEntity.ok(new MessageResponse("The album was inserted successfully."));
    } catch (Exception e) {
      return new CreateResponseFromExceptionFactory(
          new InternalServerErrorException("An error happened in the server while trying to parse the request!")
      ).create().getData();
    }
  }
}
