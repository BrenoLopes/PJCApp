package br.balladesh.pjcappbackend.dto.api;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiMessageTest {

  @Test
  void getMessage() {
    String expected = "A message to test";
    ApiMessage classToTest = new ApiMessage(expected, null, null, null);
    String received = classToTest.getMessage();

    assertEquals(expected, received);
  }

  @Test
  void getApiSecurityEndpoints() {
    ImmutableMap<String, Endpoint> expected = ImmutableMap.of("test", new Endpoint("/api/test", "get"));
    ApiMessage classToTest = new ApiMessage(null, expected, null, null);
    ImmutableMap<String, Endpoint> received = classToTest.getApiSecurityEndpoints();

    assertEquals(expected, received);
  }

  @Test
  void getApiArtistsEndpoints() {
    ImmutableMap<String, Endpoint> expected = ImmutableMap.of("test", new Endpoint("/api/test", "get"));
    ApiMessage classToTest = new ApiMessage(null, null, expected, null);
    ImmutableMap<String, Endpoint> received = classToTest.getApiArtistsEndpoints();

    assertEquals(expected, received);
  }

  @Test
  void getApiAlbumsEndpoints() {
    ImmutableMap<String, Endpoint> expected = ImmutableMap.of("test", new Endpoint("/api/test", "get"));
    ApiMessage classToTest = new ApiMessage(null, null, null, expected);
    ImmutableMap<String, Endpoint> received = classToTest.getApiAlbumsEndpoints();

    assertEquals(expected, received);
  }

  @Test
  void getValueFromNulls() {
    ImmutableMap<String, Endpoint> expectedSecurityEndpoint = ImmutableMap.of();
    ImmutableMap<String, Endpoint> expectedArtistsEndpoint = ImmutableMap.of();
    ImmutableMap<String, Endpoint> expectedAlbumsEndpoint = ImmutableMap.of();

    ApiMessage classToTest = new ApiMessage(null, null, null, null);

    assertEquals(Defaults.DEFAULT_STR, classToTest.getMessage());
    assertEquals(expectedSecurityEndpoint, classToTest.getApiSecurityEndpoints());
    assertEquals(expectedArtistsEndpoint, classToTest.getApiArtistsEndpoints());
    assertEquals(expectedAlbumsEndpoint, classToTest.getApiAlbumsEndpoints());
  }
}