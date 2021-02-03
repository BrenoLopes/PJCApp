package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.ArtistRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ArtistsServiceTest {
  @Mock
  private ArtistRepository artistRepository;

  private final UserEntity owner = new UserEntity("robot", "robot@robocop.com", "123456");

  @Test
  public void internalServerErrorWhenGettingAllArtists() {
    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistRepository).findAllByOwner(any(), any());

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.getAllArtists(owner, 0, 10, "asc");
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successWhenGettingAllArtistsInAsc() {
    List<ArtistEntity> content = Lists.newArrayList();
    Page<ArtistEntity> expected = new PageImpl<>(content, PageRequest.of(0, 10, Sort.by("name")), content.size());

    Mockito
        .when(this.artistRepository.findAllByOwner(any(), any()))
        .thenReturn(expected);

    ArtistsService testTarget = new ArtistsService(this.artistRepository);

    Page<ArtistEntity> received = testTarget.getAllArtists(owner, 0, 10, "asc");

    assertEquals(expected, received);
  }

  @Test
  public void successWhenGettingAllArtistsInDesc() {
    List<ArtistEntity> content = Lists.newArrayList();
    Page<ArtistEntity> expected = new PageImpl<>(
        content,
        PageRequest.of(0, 10, Sort.by("name").descending()),
        content.size()
    );

    Mockito
        .when(this.artistRepository.findAllByOwner(any(), any()))
        .thenReturn(expected);

    ArtistsService testTarget = new ArtistsService(this.artistRepository);
    Page<ArtistEntity> received = testTarget.getAllArtists(owner, 0, 10, "desc");

    assertEquals(expected, received);
  }

  @Test
  public void internalServerErrorWhenSearchingAnArtistById() {
    long id = 1;

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistRepository).findByIdAndNameAndOwner(id, null, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.searchAnArtist(id, owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void notFoundErrorWhenSearchingAnArtistById() {
    long id = 1;

    Mockito
        .when(this.artistRepository.findByIdAndNameAndOwner(id, null, owner))
        .thenReturn(Optional.empty());

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.searchAnArtist(id, owner);
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void successWhenSearchingAnArtistById() {
    long id = 1;

    ArtistEntity expected = new ArtistEntity("RobotDoingCaptcha", Lists.newArrayList(), owner);

    Mockito
        .when(this.artistRepository.findByIdAndNameAndOwner(id, null, owner))
        .thenReturn(Optional.of(expected));

    ArtistsService testTarget = new ArtistsService(this.artistRepository);
    ArtistEntity result = testTarget.searchAnArtist(id, owner);

    assertEquals(expected, result);
  }

  @Test
  public void internalServerErrorWhenSearchingAnArtistByName() {
    String name = "RobotDoingCaptcha";

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistRepository).findByIdAndNameAndOwner(null, name, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.searchAnArtist(name, owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void notFoundErrorWhenSearchingAnArtistByName() {
    String name = "RobotDoingCaptcha";

    Mockito
        .doReturn(Optional.empty())
        .when(this.artistRepository).findByIdAndNameAndOwner(null, name, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.searchAnArtist(name, owner);
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void successWhenSearchingAnArtistByName() {
    String name = "RobotDoingCaptcha";

    ArtistEntity expected = new ArtistEntity(name, Lists.newArrayList(), owner);

    Mockito
        .when(this.artistRepository.findByIdAndNameAndOwner(null, name, owner))
        .thenReturn(Optional.of(expected));

    ArtistsService testTarget = new ArtistsService(this.artistRepository);
    ArtistEntity result = testTarget.searchAnArtist(name, owner);

    assertEquals(expected, result);
  }

  @Test
  public void badRequestWhenAddingArtist() {
    Executable fnNameIsNull = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.addArtist(null, owner);
    };

    assertThrows(BadRequestException.class, fnNameIsNull);

    Executable fnOwnerIsNull = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.addArtist("RobotDoingCaptcha", null);
    };

    assertThrows(BadRequestException.class, fnOwnerIsNull);
  }

  @Test
  public void InternalServerErrorWhenAddingArtist() {
    String name = "RobotDoingCaptcha";

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistRepository).findByNameAndOwner(name, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.addArtist(name, owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void conflictExceptionWhenAddingArtists() {
    String name = "RobotDoingCaptcha";
    ArtistEntity expected = new ArtistEntity(name, Lists.newArrayList(), owner);

    Mockito
        .doReturn(Optional.of(expected))
        .when(this.artistRepository).findByNameAndOwner(name, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.addArtist(name, owner);
    };

    assertThrows(ConflictException.class, fn);
  }

  @Test
  public void successWhenAddingArtists() {
    String name = "RobotDoingCaptcha";
    ArtistEntity expected = new ArtistEntity(name, Lists.newArrayList(), owner);

    Mockito
        .doReturn(Optional.empty())
        .when(this.artistRepository).findByNameAndOwner(name, owner);

    Mockito
        .doReturn(expected)
        .when(this.artistRepository).save(expected);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.addArtist(name, owner);
    };

    assertDoesNotThrow(fn);
  }

  @Test
  public void notFoundErrorWhenSettingAnArtistWithId() {
    long id = 1;

    Mockito
        .doReturn(Optional.empty())
        .when(this.artistRepository).findByIdAndOwner(id, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.setAnArtist(id, Optional.of("RobotDoingDelivery"), Optional.empty(), owner);
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void InternalServerErrorWhenSettingAnArtistWithId() {
    long id = 1;

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistRepository).findByIdAndOwner(id, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.setAnArtist(id, Optional.of("RobotDoingDelivery"), Optional.empty(), owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successWhenSettingAnArtistWithId() {
    final long id = 1;

    String name = "RobotDoingCaptcha";
    ArtistEntity expected = new ArtistEntity(name, owner);

    String newName = "RobotDoingDelivery";
    List<AlbumEntity> lists = Lists.newArrayList(new AlbumEntity("Hiii", expected, ""));

    ArtistEntity modified = new ArtistEntity(newName, owner);
    modified.addAlbums(lists);

    Mockito
        .doReturn(Optional.of(expected))
        .when(this.artistRepository).findByIdAndOwner(id, owner);

    Mockito
        .doReturn(modified)
        .when(this.artistRepository).save(modified);

    ArtistsService testTarget = new ArtistsService(this.artistRepository);
    boolean result = testTarget.setAnArtist(id, Optional.of(newName), Optional.of(lists), owner);

    assertTrue(result);
  }

  @Test
  public void InternalServerErrorWhenRemovingAnArtist () {
    long id = 1;

    Mockito
        .doThrow(new InternalServerErrorException("Whoops"))
        .when(this.artistRepository).deleteByIdAndOwner(id, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.removeAnArtist(id, owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successWhenRemovingAnArtist () {
    final long id = 1;

    Mockito
        .doNothing()
        .when(this.artistRepository).deleteByIdAndOwner(id, owner);

    Executable fn = () -> {
      ArtistsService testTarget = new ArtistsService(this.artistRepository);
      testTarget.removeAnArtist(id, owner);
    };

    assertDoesNotThrow(fn);
  }
}