package br.balladesh.pjcappbackend.config.minio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfiguration {
  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.rootuser}")
  private String accessKey;

  @Value("${minio.rootpassword}")
  private String secretKey;

  @Value("${minio.bucketname}")
  private String bucketName;

  @Bean
  public MinIOEndpoint loadMinIOEndpointConfiguration() {
    return new MinIOEndpoint(
        this.endpoint,
        this.accessKey,
        this.secretKey,
        this.bucketName
    );
  }
}
