package br.balladesh.pjcappbackend.dto.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginRequestTest {
  @Test
  void getUsername() {
    String expected = "OhNoAmongUs";
    UserLoginRequest classToTest = new UserLoginRequest(expected, "");

    assertEquals(expected, classToTest.getUsername());
  }

  @Test
  void getNullUsername() {
    String expected = null;
    UserLoginRequest classToTest = new UserLoginRequest(expected, "");

    assertEquals("", classToTest.getUsername());
  }

  @Test
  void getPassword() {
    String expected = "OhNoSomePassword";
    UserLoginRequest classToTest = new UserLoginRequest("", expected);

    assertEquals(expected, classToTest.getPassword());
  }

  @Test
  void getNullPassword() {
    String expected = null;
    UserLoginRequest classToTest = new UserLoginRequest("", expected);

    assertEquals("", classToTest.getPassword());
  }
}