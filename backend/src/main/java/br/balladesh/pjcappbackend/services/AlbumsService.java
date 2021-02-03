package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import br.balladesh.pjcappbackend.utilities.predicates.AllNull;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.notNull;

@Service
public class AlbumsService {
  private final AlbumRepository albumRepository;
  private final MinIOService minIOService;

  @Autowired
  public AlbumsService(AlbumRepository albumRepository, MinIOService minIOService) {
    this.albumRepository = albumRepository;
    this.minIOService = minIOService;
  }

  /**
   * Get all albums from an artist
   *
   * @param artistId the artist's id
   * @param pageNumber The page to display. Must be greater then zero and lesser then the total number of pages
   * @param pageSize The quantity of items to display inside a page.
   * @param orderDirection The sorting order. Must be asc or desc.
   * @return a list of albums inside a page
   *
   * @throws BadRequestException if the owner is null
   * @throws InternalServerErrorException If an error happened in the process
   */
  public Page<AlbumEntity> getAllAlbumsFromArtist(long artistId, UserEntity owner, int pageNumber, int pageSize, String orderDirection) throws BadRequestException, InternalServerErrorException {
    if (owner == null)
      throw new BadRequestException("The owner cannot be null");

    try {
      Sort sort = Sort.by("name");

      if (this.isInDescendingOrder(orderDirection))
        sort.descending();

      Pageable page = PageRequest.of(pageNumber, pageSize, sort);
      Page<AlbumEntity> list = this.albumRepository.findAllByArtistIdAndArtistOwner(artistId, owner, page);
      return this.createMinIOUrlForEachAlbumImage(list);
    } catch (Exception e) {
      LoggerFactory.getLogger("wtf").error("Wtf", e);
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Search an album with given id and user
   *
   * @param albumId the album's id to be searched
   * @param owner the user who owns this album
   * @return the AlbumEntity
   *
   * @throws NotFoundException if an album with this id and owner doesn't exist
   * @throws BadRequestException if the owner is null
   * @throws InternalServerErrorException if an error happens in the process
   */
  public AlbumEntity searchAnAlbum(long albumId, UserEntity owner) throws BadRequestException, NotFoundException, InternalServerErrorException {
    if (this.isOneOfThemNull(owner))
      throw new BadRequestException("The owner cannot be null");

    try {
      AlbumEntity album = this.albumRepository.findByIdAndArtistOwner(albumId, owner)
          .orElseThrow(NotFoundException::new);

      return this.createMinIOUrlFromOneAlbum(album);
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Search an album with given name and user
   *
   * @param name the album's name to be searched
   * @param owner the user who owns this album
   * @return the AlbumEntity
   *
   * @throws NotFoundException if an album with this id and owner doesn't exist
   * @throws BadRequestException if one parameter is null
   * @throws InternalServerErrorException if an error happens in the process
   */
  public AlbumEntity searchAnAlbum(String name, UserEntity owner) throws BadRequestException, NotFoundException, InternalServerErrorException {
    if (this.isOneOfThemNull(name, owner))
      throw new BadRequestException("No parameter can be null!");

    try {
      AlbumEntity album = this.albumRepository.findByNameAndArtistOwner(name, owner)
          .orElseThrow(NotFoundException::new);

      return this.createMinIOUrlFromOneAlbum(album);
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Will search an album with one information or a combination of them.
   *
   * @param owner The user who inserted the album
   * @param pageNumber the page index you want to see
   * @param pageSize the maximum size of a page
   * @param orderDirection the order you want to sort it
   * @param id Optional - the album's id
   * @param name Optional - the album's name
   * @param artistName Optional - Album's owned by this author
   * @return A page containing a list from the result
   *
   * @throws BadRequestException if the owner is null
   * @throws InternalServerErrorException if an error happens in the process
   */
  public Page<AlbumEntity> searchAnAlbumByMultipleParameters(
      UserEntity owner,
      int pageNumber,
      int pageSize,
      String orderDirection,
      Optional<Long> id,
      Optional<String> name,
      Optional<String> artistName
  ) throws BadRequestException, InternalServerErrorException {
    if (owner == null)
      throw new BadRequestException("The owner cannot be null");

    try {
      Sort sort = Sort.by("name");

      if (this.isInDescendingOrder(orderDirection))
        sort.descending();

      Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

      if (!id.isPresent() && !name.isPresent() && !artistName.isPresent())
        return new PageImpl<>(new ArrayList<>(), pageable, 0);

      Long theId = id.orElse(null);
      String theName = name.orElse(null);
      String theArtistName = artistName.orElse(null);

      Page<AlbumEntity> resultPage = this.albumRepository.findByIdAndNameAndArtist_NameAndArtistOwner(theId, theName, theArtistName, owner, pageable);

      return this.createMinIOUrlForEachAlbumImage(resultPage);
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Add an album with the given information
   *
   * @param artistEntity the artist who owns this album
   * @param owner the user who owns this artist
   * @param name the album's name
   * @param image the album's image
   * @return true if successful, false if the transaction fails
   *
   * @throws ConflictException if the album already exists
   * @throws BadRequestException if any parameter is null
   * @throws InternalServerErrorException if an error happens in the process
   */
  public boolean addAnAlbum(ArtistEntity artistEntity, UserEntity owner, String name, MultipartFile image) throws ConflictException, InternalServerErrorException, BadRequestException {
    if (this.isOneOfThemNull(artistEntity, owner, name, image))
      throw new BadRequestException("No parameter can be null!");

    try {
      if (this.albumRepository.findByNameAndArtistOwner(name, owner).isPresent())
        throw new ConflictException("An album with this name already exists!");

      String fileName = this.minIOService.uploadFile(image);
      AlbumEntity theEntity = new AlbumEntity(name, artistEntity, fileName);

      return theEntity.equals(this.albumRepository.save(theEntity));
    } catch (ConflictException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Updates information about an album.
   *
   * @param albumId the album's id
   * @param owner the album's owner
   * @param newName the new name for the album
   * @param newImage the new image for the album
   * @return true if successful, false otherwise
   *
   * @throws NotFoundException if the album doesn't exist
   * @throws InternalServerErrorException if an error happens in the process
   */
  public boolean setAnAlbum(long albumId, UserEntity owner, String newName, MultipartFile newImage) throws NotFoundException, InternalServerErrorException {
    if (this.isAllOfThemNull(owner, newName, newImage))
      return false;

    try {
      AlbumEntity theEntity = this.albumRepository.findByIdAndArtistOwner(albumId, owner)
          .orElseThrow(NotFoundException::new);

      if (newName != null)
        theEntity.setName(newName);

      if (newImage != null && !newImage.isEmpty()) {
        String fileName = this.minIOService.uploadFile(newImage);
        theEntity.setImage(fileName);
      }

      return theEntity.equals(this.albumRepository.save(theEntity));
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Saves the entity directly to the database
   *
   * @param albumEntity the album entity to persist into the database
   *
   * @return true if successfull, false otherwise
   *
   * @throws InternalServerErrorException if an error happens in the process
   */
  public boolean saveAlbumEntity(AlbumEntity albumEntity) throws InternalServerErrorException {
    if (albumEntity == null)
      return false;

    try {
      return albumEntity.equals(this.albumRepository.save(albumEntity));
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Deletes this album from the database
   *
   * @param albumId the album's id
   * @param owner the user who owns this album
   *
   * @throws NotFoundException if the album doesn't exist
   * @throws InternalServerErrorException if an error happens in the process.
   */
  public void removeAnAlbum(long albumId, UserEntity owner) throws NotFoundException, InternalServerErrorException {
    try {
      notNull(owner, "The owner cannot be null");

      AlbumEntity theEntity = this.albumRepository.findByIdAndArtistOwner(albumId, owner)
          .orElseThrow(NotFoundException::new);

      if (!theEntity.getImage().equals("")) {
        try {
          this.minIOService.removeFile(theEntity.getImage());
        } catch (NotFoundException ignored) {}
      }

      this.albumRepository.delete(theEntity);
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  private boolean isInDescendingOrder(String direction) {
    return direction != null && !direction.equalsIgnoreCase("asc");
  }

  private boolean isOneOfThemNull(Object... args) {
    return HasNull.withParams(args).check();
  }

  private boolean isAllOfThemNull(Object... args) {
    return AllNull.withParams(args).check();
  }

  private Page<AlbumEntity> createMinIOUrlForEachAlbumImage(Page<AlbumEntity> albums) throws InternalServerErrorException {
    List<AlbumEntity> newList = albums.getContent()
        .stream()
        .map(this::createMinIOUrlFromOneAlbum)
        .collect(Collectors.toList());

    return new PageImpl<>(newList, albums.getPageable(), albums.getTotalElements());
  }

  private AlbumEntity createMinIOUrlFromOneAlbum(AlbumEntity entity) throws InternalServerErrorException {
    if (entity.getImage().equals("")) return entity;

    String url = "";
    try {
      url = this.minIOService.getFile(entity.getImage());
    } catch (NotFoundException ignored) {}

    entity.setImage(url);
    return entity;
  }
}

