package br.balladesh.pjcappbackend.config.minio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application.test.properties")
class MinIOConfigurationTest {

  @Autowired
  private MinIOConfiguration minIOConfiguration;

  @Test
  void isLoadingEnvVariables() {
    MinIOEndpoint expected = new MinIOEndpoint(
        "http://test.localhost:9000",
        "TestAmongUs",
        "TestAmongUsRootPassword",
        "balladesh-pjcapp-ohno-test"
    );

    MinIOEndpoint received = this.minIOConfiguration.loadMinIOEndpointConfiguration();

    assertEquals(expected, received);
  }
}