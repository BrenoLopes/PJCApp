package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.security.UserRepository;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;


  @Test
  public void shouldFindTheUserWithId() throws InternalServerErrorException {
    final long userId = 1;
    UserEntity expected = new UserEntity(
        1L,
        "Robot",
        "robot@robot.com",
        "IHateCaptchas",
        new ArrayList<>()
    );

    Mockito
        .when(userRepository.findById(userId))
        .thenReturn(Optional.of(expected));

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    Optional<UserEntity> received = testTarget.getUserBy(userId);

    assertTrue(received.isPresent());
    assertEquals(expected, received.get());
  }

  @Test
  public void shouldNotFindTheUserWithId() throws InternalServerErrorException {
    final long userId = 1;

    Mockito
        .when(userRepository.findById(userId))
        .thenReturn(Optional.empty());

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    Optional<UserEntity> received = testTarget.getUserBy(userId);

    assertFalse(received.isPresent());
  }

  @Test
  public void shouldThrowInternalServerErrorOnGetUserWithId() {
    final long userId = 1;

    Mockito
        .when(userRepository.findById(userId))
        .thenThrow(new RuntimeException("The db is off"));

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.getUserBy(userId);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void shouldFindTheUserWithEmail() throws InternalServerErrorException {
    final long userId = 1;
    UserEntity expected = new UserEntity(
        1L,
        "Robot",
        "robot@robot.com",
        "IHateCaptchas",
        new ArrayList<>()
    );

    Mockito
        .when(userRepository.findById(userId))
        .thenReturn(Optional.of(expected));

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    Optional<UserEntity> received = testTarget.getUserBy(userId);

    assertTrue(received.isPresent());
    assertEquals(expected, received.get());
  }

  @Test
  public void shouldNotFindTheUserWithEmail() throws InternalServerErrorException, BadRequestException {
    final String userEmail = "robot@robot.com";

    Mockito
        .when(userRepository.findByEmail(userEmail))
        .thenReturn(Optional.empty());

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    Optional<UserEntity> received = testTarget.getUserBy(userEmail);

    assertFalse(received.isPresent());
  }

  @Test
  public void shouldThrowBadRequestErrorOnGetUserWithEmail() {
    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.getUserBy(null);
    };

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void shouldThrowInternalServerErrorOnGetUserWithEmail() {
    String email = "robot@robocop.com";

    Mockito
        .when(this.userRepository.findByEmail(email))
        .thenThrow(new RuntimeException("Whoops an error!"));

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.getUserBy(email);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void shouldAddNewUser() throws BadRequestException, InternalServerErrorException, ConflictException {
    final String name = "Robot";
    final String email = "robot@robot.com";
    final String password = "IHateCaptchas";
    final String encodedPassword = "2133dwdkqpwdkqwpokd";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    final UserEntity user = new UserEntity(name, email, encodedPassword, artistEntities);

    Mockito
        .when(this.passwordEncoder.encode(password))
        .thenReturn(encodedPassword);

    Mockito
        .when(userRepository.save(user))
        .thenReturn(user);

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    boolean result = testTarget.addUser(name, email, password, artistEntities);

    assertTrue(result);
  }

  @Test
  public void shouldFailToAddNewUser_DBIsDown() {
    final String name = "Robot";
    final String email = "robot@robot.com";
    final String password = "IHateCaptchas";
    final String encodedPassword = "2133dwdkqpwdkqwpokd";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    final UserEntity user = new UserEntity(name, email, encodedPassword, artistEntities);

    Mockito
        .when(this.passwordEncoder.encode(password))
        .thenReturn(encodedPassword);

    Mockito
        .when(userRepository.save(user))
        .thenThrow(new RuntimeException("Could not connect to the db."));

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.addUser(name, email, password, artistEntities);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void shouldFailToAddNewUser_InvalidEmail() {
    final String name = "Robot";
    final String email = "Robot";
    final String password = "IHateCaptchas";
//    final String encodedPassword = "2133dwdkqpwdkqwpokd";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.addUser(name, email, password, artistEntities);
    };

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void shouldFailToAddNewUser_NullParameters() {
    final String password = "IHateCaptchas";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.addUser(null, null, password, artistEntities);
    };

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void shouldFailToAddNewUser_UserAlreadyExists() {
    final String name = "robot";
    final String email = "robot@robocop.com";
    final String password = "ihatecaptcha";

    final UserEntity firstUser = new UserEntity(name, email, password, Lists.newArrayList());

    Mockito
        .when(this.userRepository.findByEmail(email))
        .thenReturn(Optional.of(firstUser));

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.addUser(name, email, password, Lists.newArrayList());
    };

    assertThrows(ConflictException.class, fn);
  }

  @Test
  public void shouldEditUser_TheName() throws BadRequestException, InternalServerErrorException, NotFoundException {
    final long userId = 1;

    final String name = "Robot";
    final String email = "robot@robot.com";
    final String encodedPassword = "2133dwdkqpwdkqwpokd";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    final String newName = "JapaneseRobot";

    final UserEntity originalUser = new UserEntity(name, email, encodedPassword, artistEntities);
    final UserEntity newUser = new UserEntity(newName, email, encodedPassword, artistEntities);

    Mockito
        .when(this.userRepository.findById(userId))
        .thenReturn(Optional.of(originalUser));

    Mockito
        .when(this.userRepository.save(newUser))
        .thenReturn(newUser);

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    boolean result = testTarget.editUser(userId, newName, null, null, null);

    assertTrue(result);
  }

  @Test
  public void shouldEditUser_TheEmail() throws BadRequestException, InternalServerErrorException, NotFoundException {
    final long userId = 1;

    final String name = "Robot";
    final String email = "robot@robot.com";
    final String encodedPassword = "2133dwdkqpwdkqwpokd";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    final String newEmail = "robot2@robot.com";

    final UserEntity originalUser = new UserEntity(name, email, encodedPassword, artistEntities);
    final UserEntity newUser = new UserEntity(name, newEmail, encodedPassword, artistEntities);

    Mockito
        .when(this.userRepository.findById(userId))
        .thenReturn(Optional.of(originalUser));

    Mockito
        .when(this.userRepository.save(newUser))
        .thenReturn(newUser);

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    boolean result = testTarget.editUser(userId, null, newEmail, null, null);

    assertTrue(result);
  }

  @Test
  public void shouldEditUser_ThePassword() throws BadRequestException, InternalServerErrorException, NotFoundException {
    final long userId = 1;

    final String name = "Robot";
    final String email = "robot@robot.com";
    final String encodedPassword = "2133dwdkqpwdkqwpokd";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    final String newPassword = "icandocaptcha";
    final String newEncodedPassword = "239ud2jo2kj3kjopkweodkwd23id2i";

    final UserEntity originalUser = new UserEntity(name, email, encodedPassword, artistEntities);
    final UserEntity newUser = new UserEntity(name, email, newEncodedPassword, artistEntities);

    Mockito
        .when(this.passwordEncoder.encode(newPassword))
        .thenReturn(newEncodedPassword);

    Mockito
        .when(this.userRepository.findById(userId))
        .thenReturn(Optional.of(originalUser));

    Mockito
        .when(this.userRepository.save(newUser))
        .thenReturn(newUser);

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    boolean result = testTarget.editUser(userId, null, null, newPassword, null);

    assertTrue(result);
  }

  @Test
  public void shouldEditUser_TheArtists() throws BadRequestException, InternalServerErrorException, NotFoundException {
    final long userId = 1;

    final String name = "Robot";
    final String email = "robot@robot.com";
    final String encodedPassword = "2133dwdkqpwdkqwpokd";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    final UserEntity originalUser = new UserEntity(name, email, encodedPassword, artistEntities);

    final List<ArtistEntity> newArtistEntities = Lists.newArrayList(
        new ArtistEntity("myusb", Lists.newArrayList(), originalUser)
    );

    final UserEntity newUser = new UserEntity(name, email, encodedPassword, newArtistEntities);

    Mockito
        .when(this.userRepository.findById(userId))
        .thenReturn(Optional.of(originalUser));

    Mockito
        .when(this.userRepository.save(newUser))
        .thenReturn(newUser);

    UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
    boolean result = testTarget.editUser(userId, null, null, null, newArtistEntities);

    assertTrue(result);
  }

  @Test
  public void shouldNotEditUser_InternalServerError() {
    final long userId = 1;

    final String newName = "Robocop";

    UserEntity user = new UserEntity("Robot", "robot@robot.com", "123456robot");
    UserEntity newUser = new UserEntity(newName, "robot@robot.com", "123456robot");

    Mockito
        .when(this.userRepository.findById(userId))
        .thenReturn(Optional.of(user));

    Mockito
        .when(this.userRepository.save(newUser))
        .thenThrow(new RuntimeException("Whoops!"));

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.editUser(userId, newName, null, null, null);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void shouldNotEditUser_NotFound() {
    final long userId = 1;

    final String password = "IHateCaptchas";
    final List<ArtistEntity> artistEntities = Lists.newArrayList();

    Mockito
        .when(this.userRepository.findById(userId))
        .thenReturn(Optional.empty());

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.editUser(1, null, null, password, artistEntities);
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void shouldNotEditUser_InvalidEmail() {
    final String email = "robot";

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.editUser(1, null, email, null, null);
    };

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void shouldNotEditUser_AllNull() {
    final long userId = 1;

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.editUser(userId, null, null, null, null);
    };

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void shouldDeleteUser() {
    final long userId = 1;

    Mockito
        .doNothing()
        .when(this.userRepository)
        .deleteById(userId);

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.removeUser(userId);
    };

    assertDoesNotThrow(fn);
  }

  @Test
  public void shouldNotDeleteUser_ServerError() {
    final long userId = 1;

    Mockito
        .doThrow(new RuntimeException("An error!"))
        .when(this.userRepository)
        .deleteById(userId);

    Executable fn = () -> {
      UsersService testTarget = new UsersService(this.userRepository, this.passwordEncoder);
      testTarget.removeUser(userId);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }
}