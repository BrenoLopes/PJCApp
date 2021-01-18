package br.balladesh.pjcappbackend.dto.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtJsonResponseTest {

  @Test
  void getUsername() {
    String expected = "OhNoAmongUs";
    JwtJsonResponse classToTest = new JwtJsonResponse(expected, "");

    assertEquals(expected, classToTest.getUsername());
  }

  @Test
  void getNullUsername() {
    String expected = null;
    JwtJsonResponse classToTest = new JwtJsonResponse(expected, "");

    assertEquals("", classToTest.getUsername());
  }

  @Test
  void getToken() {
    String expected = "ImagineThatThisIsAValidJwtToken";
    JwtJsonResponse classToTest = new JwtJsonResponse("", expected);

    assertEquals(expected, classToTest.getToken());
  }

  @Test
  void getNullToken() {
    String expected = null;
    JwtJsonResponse classToTest = new JwtJsonResponse("", expected);

    assertEquals("", classToTest.getToken());
  }
}