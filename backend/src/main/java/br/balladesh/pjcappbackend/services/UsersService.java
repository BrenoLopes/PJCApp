package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.controllers.exceptions.*;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.security.UserRepository;
import br.balladesh.pjcappbackend.utilities.predicates.AllNull;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsersService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsersService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Search for a user with the given parameter
   *
   * @param id The user's id to search for
   *
   * @throws br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException if any error happens in the
   * process
   *
   * @return The user entity or Option.empty if the user doesn't exists
   */
  public Optional<UserEntity> getUserBy(long id) throws InternalServerErrorException {
    try {
      return this.userRepository.findById(id);
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Search for a user with the given parameter
   *
   * @param email The user's email to search for
   *
   * @throws br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException if any error happens in the
   * process
   * @throws BadRequestException if the email is null
   *
   * @return The user entity or Option.empty if the user doesn't exists
   */
  public Optional<UserEntity> getUserBy(String email) throws InternalServerErrorException, BadRequestException {
    if (email == null)
      throw new BadRequestException("The email cannot be empty!");

    try {
      return this.userRepository.findByEmail(email);
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Load a user entity based on the user created by the security
   * inside the security context.
   *
   * @throws InternalServerErrorException If an error happens in the process or the user
   * in the security context is in another format
   *
   * @return The user entity from the database
   */
  public Optional<UserEntity> getCurrentAuthenticatedUser() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (!(authentication.getPrincipal() instanceof UserDetails))
        throw new RuntimeException("The context wasn't loaded properly");

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      return this.userRepository.findByEmail(userDetails.getUsername());
    } catch (RuntimeException e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Add a user with this parameters to the database
   *
   * @param name The user's name
   * @param email The user's email address
   * @param password The user's password
   * @param artistEntities The user's artists
   *
   * @throws BadRequestException if the email is in an invalid format or any data is null
   * @throws ConflictException if an user already exists with this email address.
   * @throws InternalServerErrorException if any error happens in the
   * process
   *
   * @return true if successfull, false if something fails
   */
  public boolean addUser(String name, String email, String password, List<ArtistEntity> artistEntities)
      throws InternalServerErrorException, BadRequestException, ConflictException {
    if (this.isOneOfThemNull(name, email, password, artistEntities))
      throw new BadRequestException("No argument in this method must be null!");

    if (this.isEmailInvalid(email))
      throw new BadRequestException("Could not add this user, because the new email is invalid!");

    try {
      if (this.getUserBy(email).isPresent())
        throw new ConflictException("An user already exists with this email address!");
      
      String encodedPassword = this.passwordEncoder.encode(password);

      UserEntity userEntity = new UserEntity(name, email, encodedPassword, artistEntities);
      UserEntity resultEntity = this.userRepository.save(userEntity);

      return resultEntity.equals(userEntity);
    } catch (ConflictException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Will try to set a user with this information if it exists with given id.
   *
   * If the given parameter is null, it'll be skipped.
   *
   * @param id The user's id to set
   * @param name The new name
   * @param email The new email
   * @param password The new password
   * @param artistEntities The new list of artists
   *
   * @throws br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException If the given user is not found
   * @throws br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException If the email is in an invalid format
   * @throws br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException if any error happens in the
   * process
   *
   * @return true if successfull or false if the server had any error
   */
  public boolean editUser(long id, String name, String email, String password, List<ArtistEntity> artistEntities)
      throws NotFoundException, InternalServerErrorException, BadRequestException
  {
    if (this.isAllOfThemNull(name, email, password, artistEntities))
      throw new BadRequestException("There is nothing to change in the user.");

    if (email != null && this.isEmailInvalid(email))
      throw new BadRequestException("Could not edit this user, because the new email is invalid!");

    UserEntity userEntity = this.userRepository
        .findById(id)
        .orElseThrow(NotFoundException::new);

    try {
      if (name != null)
        userEntity.setName(name);

      if (email != null)
        userEntity.setEmail(email);

      if (password != null) {
        String p = this.passwordEncoder.encode(password);
        userEntity.setPassword(p);
      }

      if (artistEntities != null && artistEntities.size() > 0) {
        userEntity.getArtists().clear();
        userEntity.getArtists().addAll(artistEntities);
      }

      UserEntity result = this.userRepository.save(userEntity);

      return result.equals(userEntity);
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Try to remove an user with this id.
   *
   * @param id The user's id to be removed.
   *
   * @throws br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException if any error happens in the
   * process
   */
  public void removeUser(long id) throws InternalServerErrorException {
    try {
      this.userRepository.deleteById(id);
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  private boolean isEmailInvalid(String email) {
    String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(regex);

    return !pattern.matcher(email).matches();
  }

  private boolean isOneOfThemNull(Object... args) {
    return HasNull.withParams(args).check();
  }

  private boolean isAllOfThemNull(Object... args) {
    return AllNull.withParams(args).check();
  }
}
