package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import br.balladesh.pjcappbackend.utilities.defaults.EmptyMultipartFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditAlbumRequestBodyTest {

  @Test
  void getName() {
    String expected = "RandomName";
    EditAlbumRequestBody underTest = new EditAlbumRequestBody(
        1L,
        expected,
        new EmptyMultipartFile(),
        23L
    );

    assertEquals(expected, underTest.getName());
  }

  @Test
  void getImage() {
    EmptyMultipartFile expected = new EmptyMultipartFile();
    EditAlbumRequestBody underTest = new EditAlbumRequestBody(
        1L,
        null,
        null,
        23L
    );

    assertEquals(expected, underTest.getImage());
  }

  @Test
  void getAlbumId() {
    long expected = 123;
    EditAlbumRequestBody underTest = new EditAlbumRequestBody(
        expected,
        null,
        null,
        100L
    );

    assertEquals(expected, underTest.getAlbumId());
  }

  @Test
  void getFromNull() {
    EditAlbumRequestBody underTest = new EditAlbumRequestBody(
        null,
        null,
        null,
        null
    );

    assertEquals(Defaults.getDefaultLong(), underTest.getAlbumId());
    assertEquals(Defaults.DEFAULT_STR, underTest.getName());
    assertEquals(new EmptyMultipartFile(), underTest.getImage());
  }
}