package br.balladesh.pjcappbackend.config.minio;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinIOEndpointTest {

  @Test
  void getEndpoint() {
    String expected = "http://localhost";

    MinIOEndpoint endpoint = new MinIOEndpoint(
        expected,
        "randomaccesskey",
        "randomsecretkey",
        "bucket"
    );

    assertEquals(expected, endpoint.getEndpoint());
  }

  @Test
  void getAccessKey() {
    String expected = "randomaccesskey";

    MinIOEndpoint endpoint = new MinIOEndpoint(
        "http://localhost",
        expected,
        "randomsecretkey",
        "bucket"
    );

    assertEquals(expected, endpoint.getAccessKey());
  }

  @Test
  void getSecretKey() {
    String expected = "randomsecretkey";

    MinIOEndpoint endpoint = new MinIOEndpoint(
        "http://localhost",
        "randomaccesskey",
        expected,
        "bucket"
    );

    assertEquals(expected, endpoint.getSecretKey());
  }

  @Test
  void getBucketName() {
    String expected = "bucket";

    MinIOEndpoint endpoint = new MinIOEndpoint(
        "http://localhost",
        "randomaccesskey",
        "randomsecretkey",
        expected
    );

    assertEquals(expected, endpoint.getBucketName());
  }

  @Test
  void getFromNull() {
    MinIOEndpoint endpoint = new MinIOEndpoint(null, null, null, null);
    assertEquals(Defaults.DEFAULT_STR, endpoint.getEndpoint());
    assertEquals(Defaults.DEFAULT_STR, endpoint.getAccessKey());
    assertEquals(Defaults.DEFAULT_STR, endpoint.getSecretKey());
    assertEquals(Defaults.DEFAULT_STR, endpoint.getBucketName());
  }
}