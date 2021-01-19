package br.balladesh.pjcappbackend.dto;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {
  @Test
  void getNormalMessage() {
    String expected = "A simple Message";
    MessageResponse classToTest = new MessageResponse(expected);

    String received = classToTest.getMessage();

    assertEquals(expected, received, "The class must return the same string used in the constructor.");
  }

  @Test
  void getEmptyMessage() {
    String expected = "";
    MessageResponse classToTest = new MessageResponse(expected);

    String received = classToTest.getMessage();

    assertEquals(expected, received, "The class must return the same string used in the constructor.");
  }

  @Test
  void getNullEmptyMessage() {
    String expected = null;
    MessageResponse classToTest = new MessageResponse(expected);

    String received = classToTest.getMessage();

    assertEquals(
        Defaults.DEFAULT_STR,
        received,
        "The class must return an empty string in case a null one was used in the constructor."
    );
  }
}