package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.ConflictException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.AlbumRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AlbumsServiceTest {
  @Mock
  private AlbumRepository albumRepository;

  @Mock
  private MinIOService minIOService;

  private final UserEntity owner = new UserEntity("robot", "robot@robocop.com", "123456");

  @AfterEach
  public void cleanMocks() {
    reset(this.albumRepository, minIOService);
  }

  @Test
  public void badRequestExceptionWhenGettingAllAlbumsFromArtist() {
    long artistId = 1;

    AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);

    Executable fn = () -> testTarget.getAllAlbumsFromArtist(artistId, null, 0, 10, "asc");

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void successWhenGettingAllAlbumsFromArtist() {
    long artistId = 2;
    Pageable page = PageRequest.of(0, 10, Sort.by("name"));

    Page<AlbumEntity> expected = new PageImpl<>(Lists.newArrayList(), page, 0);

    Mockito
        .when(this.albumRepository.findAllByArtistIdAndArtistOwner(artistId, owner, page))
        .thenReturn(expected);

    AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
    Page<AlbumEntity> result = testTarget.getAllAlbumsFromArtist(
        artistId,
        owner,
        0,
        10,
        "asc"
    );

    assertEquals(expected, result);
  }

  @Test
  public void successWhenGettingAllAlbumsFromArtistWithMinIOUrl() {
    long artistId = 1;
    Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

    List<AlbumEntity> content = Lists.newArrayList(
        new AlbumEntity("ohno", new ArtistEntity(), "randomimage")
    );
    Page<AlbumEntity> dbExpected = new PageImpl<>(content, pageable, content.size());

    String minIOFakeName = "12312312312-randomimage";
    List<AlbumEntity> minIOContent = Lists.newArrayList(
        new AlbumEntity("ohno", new ArtistEntity(), minIOFakeName)
    );
    Page<AlbumEntity> minIOExpected = new PageImpl<>(minIOContent, pageable, content.size());

    Mockito
        .when(this.albumRepository.findAllByArtistIdAndArtistOwner(artistId, owner, pageable))
        .thenReturn(dbExpected);

    Mockito
        .when(this.minIOService.getFile("randomimage"))
        .thenReturn(minIOFakeName);

    AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
    Page<AlbumEntity> result = testTarget.getAllAlbumsFromArtist(artistId, owner, 0, 10, "asc");

    assertEquals(minIOExpected, result);
  }

  @Test
  public void notFoundWhenSearchAnAlbumById() {
    final long albumId = 1;

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenReturn(Optional.empty());

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.searchAnAlbum(albumId, owner);
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void badRequestWhenSearchAnAlbumById() {
    final long albumId = 1;

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.searchAnAlbum(albumId, null);
    };

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void internalServerErrorWhenSearchAnAlbumById() {
    final long albumId = 1;

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenThrow(new InternalServerErrorException("Whoops"));

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.searchAnAlbum(albumId, owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successfulWhenSearchAnAlbumById() {
    final long albumId = 1;
    final String imageName = "TheRobot";
    final String minIOImageName = "123132-TheRobot";

    AlbumEntity expected = new AlbumEntity(
        "RobotDoingDelivery",
        new ArtistEntity("Robot", Lists.newArrayList(), owner),
        imageName
    );

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenReturn(Optional.of(expected));

    Mockito
        .when(this.minIOService.getFile(imageName))
        .thenReturn(minIOImageName);

    AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
    AlbumEntity received = testTarget.searchAnAlbum(albumId, owner);

    assertEquals(expected, received);
  }

  @Test
  public void notFoundWhenSearchAnAlbumByName() {
    final String albumName = "RobotDoingDelivery";

    Mockito
        .when(this.albumRepository.findByNameAndArtistOwner(albumName, owner))
        .thenReturn(Optional.empty());

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.searchAnAlbum(albumName, owner);
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void badRequestWhenSearchAnAlbumByName() {
    final String albumName = "RobotDoingDelivery";

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.searchAnAlbum(albumName, null);
    };

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void internalServerErrorWhenSearchAnAlbumByName() {
    final String albumName = "RobotDoingDelivery";

    Mockito
        .when(this.albumRepository.findByNameAndArtistOwner(albumName, owner))
        .thenThrow(new InternalServerErrorException("Whoops"));

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.searchAnAlbum(albumName, owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successfulWhenSearchAnAlbumByName() {
    final String albumName = "RobotDoingDelivery";
    final String imageName = "TheRobot";
    final String minIOImageName = "123132-TheRobot";

    AlbumEntity expected = new AlbumEntity(
        "RobotDoingDelivery",
        new ArtistEntity("Robot", Lists.newArrayList(), owner),
        imageName
    );

    Mockito
        .when(this.albumRepository.findByNameAndArtistOwner(albumName, owner))
        .thenReturn(Optional.of(expected));

    Mockito
        .when(this.minIOService.getFile(imageName))
        .thenReturn(minIOImageName);

    AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
    AlbumEntity received = testTarget.searchAnAlbum(albumName, owner);

    assertEquals(expected, received);
  }

  @Test
  public void conflictWhenAddingAnAlbum() {
    final String albumName = "RobotDoingDelivery";
    final ArtistEntity theArtist = new ArtistEntity("AA", Lists.newArrayList(), owner);

    AlbumEntity theAlbum = new AlbumEntity(albumName, theArtist, "robot");

    Mockito
        .when(this.albumRepository.findByNameAndArtistOwner(albumName, owner))
        .thenReturn(Optional.of(theAlbum));

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.addAnAlbum(
          theArtist,
          owner,
          albumName,
          new MockMultipartFile(theAlbum.getImage(), new byte[0])
      );
    };

    assertThrows(ConflictException.class, fn);
  }

  @Test
  public void badRequestWhenAddingAnAlbum() {
    AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);

    Executable fn1 = () -> testTarget.addAnAlbum(
        null,
        owner,
        "RobotDoingDelivery",
        new MockMultipartFile("robot", new byte[0])
    );

    assertThrows(BadRequestException.class, fn1);

    Executable fn2 = () -> testTarget.addAnAlbum(
        new ArtistEntity("AA", Lists.newArrayList(), owner),
        null,
        "RobotDoingDelivery",
        new MockMultipartFile("robot", new byte[0])
    );

    assertThrows(BadRequestException.class, fn2);

    Executable fn3 = () -> testTarget.addAnAlbum(
        new ArtistEntity("AA", Lists.newArrayList(), owner),
        owner,
        null,
        new MockMultipartFile("robot", new byte[0])
    );

    assertThrows(BadRequestException.class, fn3);

    Executable fn4 = () -> testTarget.addAnAlbum(
        new ArtistEntity("AA", Lists.newArrayList(), owner),
        owner,
        "RobotDoingDelivery",
        null
    );

    assertThrows(BadRequestException.class, fn4);
  }

  @Test
  public void internalServerErrorWhenAddingAnAlbum() {
    final String albumName = "RobotDoingDelivery";
    final ArtistEntity theArtist = new ArtistEntity("AA", Lists.newArrayList(), owner);

    AlbumEntity theAlbum = new AlbumEntity(albumName, theArtist, "robot");

    Mockito
        .when(this.albumRepository.findByNameAndArtistOwner(albumName, owner))
        .thenThrow(new InternalServerErrorException("Whoops"));

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.addAnAlbum(
          theArtist,
          owner,
          albumName,
          new MockMultipartFile(theAlbum.getImage(), new byte[0])
      );
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successWhenAddingAnAlbum() {
    final String albumName = "RobotDoingDelivery";
    final ArtistEntity theArtist = new ArtistEntity("AA", Lists.newArrayList(), owner);

    MultipartFile theFile = new MockMultipartFile("robot", new byte[0]);

    String theMinIOFileName = "23133123123-" + theFile.getOriginalFilename();
    AlbumEntity theNewAlbum = new AlbumEntity(albumName, theArtist, theMinIOFileName);

    Mockito
        .when(this.albumRepository.findByNameAndArtistOwner(albumName, owner))
        .thenReturn(Optional.empty());

    Mockito
        .when(this.minIOService.uploadFile(theFile))
        .thenReturn(theMinIOFileName);

    Mockito
        .when(this.albumRepository.save(theNewAlbum))
        .thenReturn(theNewAlbum);

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.addAnAlbum(theArtist, owner, albumName, theFile);
    };

    assertDoesNotThrow(fn);
  }

  @Test
  public void notFoundWhenEditingAnAlbum() {
    final long albumId = 1;
    final String newName = "Robot, James Robot";

    AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenReturn(Optional.empty());

    Executable fn = () -> testTarget.setAnAlbum(albumId, owner, newName, new MockMultipartFile("robot", new byte[0]));

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void internalServerErrorWhenEditingAnAlbum() {
    final long albumId = 1;

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenThrow(new InternalServerErrorException("WHoops"));

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.setAnAlbum(albumId, owner, "newname", null);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successWhenSettingAnAlbum() {
    final long albumId = 1;
    final String newName = "Robot, James Robot";

    MultipartFile theFile = new MockMultipartFile(
        "Ohno",
        "Ohno",
        "image/png",
        new byte[10]
    );

    ArtistEntity theArtist = new ArtistEntity("Robocop", Lists.newArrayList(), owner);
    AlbumEntity theEntity = new AlbumEntity("TheRobots", theArtist, theFile.getOriginalFilename());

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenReturn(Optional.of(theEntity));

    final String newMinIOFileName = "1231312312-Ohno";

    Mockito
        .when(this.minIOService.uploadFile(any()))
        .thenReturn(newMinIOFileName);

    AlbumEntity newAlbum = new AlbumEntity(newName, theEntity.getArtist(), newMinIOFileName);

    Mockito
        .when(this.albumRepository.save(newAlbum))
        .thenReturn(newAlbum);

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.setAnAlbum(albumId, owner, newName, theFile);
    };

    assertDoesNotThrow(fn);
  }

  @Test
  public void notFoundWhenRemovingAlbum() {
    final long albumId = 1;

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenReturn(Optional.empty());

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.removeAnAlbum(albumId, owner);
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void internalServerErrorWhenRemovingAnAlbum() {
    final long albumId = 1;

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenThrow(new InternalServerErrorException("Whoops"));

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.removeAnAlbum(albumId, owner);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successWhenRemovingAnAlbum() {
    final long albumId = 1;

    ArtistEntity theArtist = new ArtistEntity("Robocop", Lists.newArrayList(), owner);
    AlbumEntity theEntity = new AlbumEntity("TheRobots", theArtist, "randomfile");

    Mockito
        .when(this.albumRepository.findByIdAndArtistOwner(albumId, owner))
        .thenReturn(Optional.of(theEntity));

    Mockito
        .doNothing()
        .when(this.minIOService).removeFile(theEntity.getImage());

    Mockito
        .doNothing()
        .when(this.albumRepository).delete(theEntity);

    Executable fn = () -> {
      AlbumsService testTarget = new AlbumsService(this.albumRepository, this.minIOService);
      testTarget.removeAnAlbum(albumId, owner);
    };

    assertDoesNotThrow(fn);
  }
}