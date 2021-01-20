package br.balladesh.pjcappbackend.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UploadConfigurationTest {

  @Autowired
  private UploadConfiguration uploadConfiguration;

  @Test
  public void testUploadSize() {
    long expected = 20 * 1024 * 1024;
    long result = this.uploadConfiguration.multipartConfigElement().getMaxFileSize();

    assertEquals(expected, result);
  }
}