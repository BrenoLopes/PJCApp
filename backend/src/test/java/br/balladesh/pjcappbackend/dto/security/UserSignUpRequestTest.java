package br.balladesh.pjcappbackend.dto.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSignUpRequestTest {

  @Test
  void getUsername() {
    String expected = "OhNoAmongUs";
    UserSignUpRequest classToTest = new UserSignUpRequest(expected, "", "");

    assertEquals(expected, classToTest.getUsername());
  }

  @Test
  void getNullUsername() {
    String expected = null;
    UserSignUpRequest classToTest = new UserSignUpRequest(expected, "", "");

    assertEquals("", classToTest.getUsername());
  }

  @Test
  void getName() {
    String expected = "OhNo";
    UserSignUpRequest classToTest = new UserSignUpRequest("", expected, "");

    assertEquals(expected, classToTest.getName());
  }

  @Test
  void getNullName() {
    String expected = null;
    UserSignUpRequest classToTest = new UserSignUpRequest("", expected, "");

    assertEquals("", classToTest.getName());
  }

  @Test
  void getPassword() {
    String expected = "OhNoAmongUsPassword";
    UserSignUpRequest classToTest = new UserSignUpRequest("", "", expected);

    assertEquals(expected, classToTest.getPassword());
  }

  @Test
  void getNullPassword() {
    String expected = null;
    UserSignUpRequest classToTest = new UserSignUpRequest("", "", expected);

    assertEquals("", classToTest.getPassword());
  }
}