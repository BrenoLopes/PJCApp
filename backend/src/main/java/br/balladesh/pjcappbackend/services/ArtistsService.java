package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.Assert.*;

@Service
public class ArtistsService {
  private final ArtistRepository artistRepository;

  @Autowired
  public ArtistsService(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  /**
   * Get a list constrained by the number of items and the order of the items.
   *
   * @param pageNumber The page to display. Must be greater then zero and lesser then the total number of pages
   * @param pageSize The quantity of items to display inside a page.
   * @param orderDirection The sorting order. Must be asc or desc.
   * @throws InternalServerErrorException If an error happened in the process
   *
   * @return a list of artists inside a page
   */
  public Page<ArtistEntity> getAllArtists(UserEntity owner, int pageNumber, int pageSize, String orderDirection)
      throws InternalServerErrorException
  {
    try {
      Sort sort = Sort.by("name");

      if (this.isInDescendingOrder(orderDirection))
        sort.descending();

      Pageable page = PageRequest.of(pageNumber, pageSize, sort);

      return this.artistRepository.findAllByOwner(owner, page);
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Search for an artist with an id.
   *
   * @param id the artist's id. If null nothing will be returned
   * @return a list of artists inside a page
   *
   * @throws NotFoundException If the artist doesn't exist
   * @throws InternalServerErrorException If an error happened in the process
   *
   */
  public ArtistEntity searchAnArtist(Long id, UserEntity owner) throws InternalServerErrorException {
    try {
      return this.artistRepository.findByIdAndNameAndOwner(id, null, owner)
          .orElseThrow(NotFoundException::new);
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Search for an artist with a name.
   *
   * @param name the artist's name. If null nothing will be returned
   * @return a list of artists inside a page
   *
   * @throws NotFoundException If the artist doesn't exists
   * @throws InternalServerErrorException If an error happened in the process
   */
  public ArtistEntity searchAnArtist(String name, UserEntity owner) {
    try {
      return this.artistRepository.findByIdAndNameAndOwner(null, name, owner)
          .orElseThrow(NotFoundException::new);
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Persist the an artist with this name and owner on the database.
   *
   * @param name The name of the artist
   * @param owner The user that is inserting this artist
   *
   * @throws ConflictException If an artist with this name already exists
   * @throws InternalServerErrorException If an error happened in the process
   * @throws BadRequestException If the name or owner is null
   */
  public void addArtist(String name, UserEntity owner) throws ConflictException, InternalServerErrorException, BadRequestException {
    if (this.isOneOfThemNull(name, owner))
      throw new BadRequestException("The name and author must not be null");

    this.addArtist(new ArtistEntity(name, new ArrayList<>(), owner));
  }

  /**
   * Persist the an artist with an entity
   *
   * @param artistEntity the entity with the artist's information
   *
   * @throws ConflictException if the artist already exist in the database
   * @throws InternalServerErrorException if an error happens in the process
   * @throws BadRequestException if the entity is null
   */
  public void addArtist(ArtistEntity artistEntity) throws ConflictException, InternalServerErrorException, BadRequestException {
    if (artistEntity == null)
      throw new BadRequestException("The name and author must not be null");

    try {
      if (this.artistRepository.findByNameAndOwner(artistEntity.getName(), artistEntity.getOwner()).isPresent())
        throw new ConflictException("This artist already exists.");

      this.artistRepository.save(artistEntity);
    } catch (ConflictException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Will update an existing artist with this new data. Be careful, because
   * this method will set the list of albums to the new list, so any previous
   * albums will be removed.
   *
   * @param id the artist's id to be updated
   * @param newName if not null, the new name to give to the artist.
   * @param newAlbums if not null, the new albums to give to the artist.
   * @param owner the user that inserted this artist
   *
   * @return true if successful, false if not.
   *
   * @throws NotFoundException if there is no artist with this id.
   * @throws InternalServerErrorException if an error happened in the process.
   */
  public boolean setAnArtist(long id, Optional<String> newName, Optional<List<AlbumEntity>> newAlbums, UserEntity owner) throws InternalServerErrorException, NotFoundException {
    try {
      notNull(owner, "The owner cannot be null");

      if (!newName.isPresent() && !newAlbums.isPresent())
        return false;

      ArtistEntity theArtist = this.artistRepository.findByIdAndOwner(id, owner)
          .orElseThrow(NotFoundException::new);

      newName.ifPresent(theArtist::setName);

      if (newAlbums.isPresent() && newAlbums.get().size() > 0) {
        theArtist.addAlbums(newAlbums.get());
      }

      return theArtist.equals(this.artistRepository.save(theArtist));
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Saves the entity directly to the database
   *
   * @param artistEntity the artist entity to persist into the database
   *
   * @return true if successfull, false otherwise
   *
   * @throws InternalServerErrorException if an error happens in the process
   */
  public boolean saveAlbumEntity(ArtistEntity artistEntity) throws InternalServerErrorException {
    if (artistEntity == null)
      return false;

    try {
      return artistEntity.equals(this.artistRepository.save(artistEntity));
    } catch (Exception e) {
      throw new InternalServerErrorException(e);
    }
  }

  /**
   * Will delete an artist with given id
   *
   * @param id the artist's id to be removed.
   *
   * @throws InternalServerErrorException if an error happens in the process.
   */
  @Transactional
  public void removeAnArtist(long id, UserEntity owner) throws InternalServerErrorException {
    try {
      this.artistRepository.deleteByIdAndOwner(id, owner);
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  private boolean isInDescendingOrder(String direction) {
    return direction != null && !direction.equalsIgnoreCase("asc");
  }

  private boolean isOneOfThemNull(Object... args) {
    return HasNull.withParams(args).check();
  }
}
