package br.balladesh.pjcappbackend.dto.api;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndpointTest {

  @Test
  void getUrl() {
    String expected = "/some/urls";
    Endpoint classToTest = new Endpoint(expected, "post");

    assertEquals(expected, classToTest.getUrl());
  }

  @Test
  void getMethod() {
    String expected = "post";
    Endpoint classToTest = new Endpoint("/some/urls", expected);

    assertEquals(expected, classToTest.getMethod());
  }

  @Test
  void testEquals() {
    Endpoint classToTest1 = new Endpoint("/some/urls", "post");
    Endpoint classToTest2 = new Endpoint("/some/urls", "post");

    assertEquals(classToTest1, classToTest2);
  }

  @Test
  void testWithNulls() {
    Endpoint classToTest = new Endpoint(null, null);

    assertEquals(Defaults.DEFAULT_STR, classToTest.getUrl());
    assertEquals(Defaults.DEFAULT_STR, classToTest.getMethod());
  }

  @Test
  void testToString() {
    String expected = "Endpoint{url=/some/urls, method=post}";
    Endpoint classToTest = new Endpoint("/some/urls", "post");
    String received = classToTest.toString();

    assertEquals(expected, received);
  }
}