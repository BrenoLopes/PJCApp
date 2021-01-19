package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.utilities.Defaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PutArtistRequestDTOTest {

  @Test
  void getName() {
    String expected = "OhNo";
    PutArtistRequestDTO testTarget = new PutArtistRequestDTO(1, expected);

    assertEquals(expected, testTarget.getName());
  }

  @Test
  void getId() {
    long expected = 1;
    PutArtistRequestDTO testTarget = new PutArtistRequestDTO(expected, "OhNo");

    assertEquals(expected, testTarget.getId());
  }

  @Test
  void getFromNull() {
    PutArtistRequestDTO testTarget = new PutArtistRequestDTO(0, null);

    assertEquals(Defaults.DEFAULT_STR, testTarget.getName());
    assertEquals(0, testTarget.getId());
  }
}