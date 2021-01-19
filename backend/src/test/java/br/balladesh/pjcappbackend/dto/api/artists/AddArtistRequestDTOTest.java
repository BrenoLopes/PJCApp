package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddArtistRequestDTOTest {

  @Test
  void getName() {
    String expected = "OhNo";
    AddArtistRequestDTO testTarget = new AddArtistRequestDTO(expected);

    assertEquals(expected, testTarget.getName());
  }

  @Test
  void getFromNull() {
    AddArtistRequestDTO testTarget = new AddArtistRequestDTO(null);

    assertEquals(Defaults.DEFAULT_STR, testTarget.getName());
  }
}