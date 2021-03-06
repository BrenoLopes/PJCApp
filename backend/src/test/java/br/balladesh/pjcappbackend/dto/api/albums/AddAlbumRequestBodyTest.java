package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import br.balladesh.pjcappbackend.utilities.defaults.EmptyMultipartFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddAlbumRequestBodyTest {

  @Test
  void getName() {
    String expected = "RandomAlbum";

    AddAlbumRequestBody classToTest = new AddAlbumRequestBody(
        expected,
        null,
        56L
    );

    assertEquals(expected, classToTest.getName());
  }

  @Test
  void getImage() {
    AddAlbumRequestBody classToTest = new AddAlbumRequestBody(
        "RandomAlbum",
        null,
        56L
    );

    assertEquals(new EmptyMultipartFile(), classToTest.getImage());
  }

  @Test
  void getArtistId() {
    long expected = 23;

    AddAlbumRequestBody classToTest = new AddAlbumRequestBody(
        "RandomAlbum",
        null,
        expected
    );

    assertEquals(expected, classToTest.getArtistId());
  }

  @Test
  void getDataFromNull() {
    AddAlbumRequestBody classToTest = new AddAlbumRequestBody(
        null,
        null,
        null
    );

    assertEquals(Defaults.DEFAULT_STR, classToTest.getName());
    assertEquals(new EmptyMultipartFile(), classToTest.getImage());
    assertEquals(Defaults.getDefaultLong(), classToTest.getArtistId());
  }
}