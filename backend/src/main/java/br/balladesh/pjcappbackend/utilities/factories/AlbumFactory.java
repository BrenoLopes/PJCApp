package br.balladesh.pjcappbackend.utilities.factories;

import br.balladesh.pjcappbackend.dto.api.albums.AddAlbumRequestBody;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.services.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.commands.UploadToMinIOCommand;
import br.balladesh.pjcappbackend.utilities.errors.BadRequestException;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AlbumFactory implements Factory<ArtistEntity, HttpException>{
  private final AddAlbumRequestBody data;
  private final ArtistRepository artistRepository;
  private final MinIOEndpoint endpoint;

  private boolean uploadNow = false;
  private final Logger logger = LoggerFactory.getLogger(AlbumFactory.class);

  private AlbumFactory(AddAlbumRequestBody data, ArtistRepository artistRepository, MinIOEndpoint endpoint) {
    this.data = data;
    this.artistRepository = artistRepository;
    this.endpoint = endpoint;
  }

  public static AlbumFactory from(AddAlbumRequestBody data, ArtistRepository artistRepository, MinIOEndpoint endpoint) {
    return new AlbumFactory(data, artistRepository, endpoint);
  }

  public AlbumFactory uploadToMinIO() {
    this.uploadNow = true;
    return this;
  }

  @Override
  public Result<ArtistEntity, HttpException> create() {
    // Check if the user already exists. If not return an exception. I don't want to create an artist from here.
    Optional<ArtistEntity> _artist = this.createArtist();
    if (!_artist.isPresent()) {
      String message = "The artist doesn't exist. Please register him before creating an album.";
      return Result.fromError(new BadRequestException(message));
    }

    String imageName = "";

    // If the user has sent an image try to persist it into MinIO if uploadNow is set
    if (this.uploadNow && !this.data.getImage().isEmpty()) {
      Result<String, HttpException> uploadResult = new UploadToMinIOCommand(this.data.getImage(), this.endpoint)
          .execute();

      if (!uploadResult.haveData()) {
        logger.error(
            "AlbumFactory::create::MinIOUploader Failed to upload the image into a MinIO instance. Error: {}",
            uploadResult.getException().getMessage()
        );

        return Result.fromError(
            new InternalServerErrorException("An error happened in the server while trying to process your request.")
        );
      }

      imageName = uploadResult.getData();
    }

    ArtistEntity artist = _artist.get();
    artist.getAlbums().add(new AlbumEntity(this.data.getName(), _artist.get(), imageName));

    // Then create an AlbumEntity with the data
    return Result.from(artist);
  }

  private Optional<ArtistEntity> createArtist() {
    if (this.data.getArtistId().isPresent()) {
      long id = this.data.getArtistId().get();
      return this.artistRepository.findById(id);
    }

    if (this.data.getArtistName().isPresent()) {
      String name = this.data.getArtistName().get();
      return this.artistRepository.findByName(name);
    }

    return Optional.empty();
  }
}
