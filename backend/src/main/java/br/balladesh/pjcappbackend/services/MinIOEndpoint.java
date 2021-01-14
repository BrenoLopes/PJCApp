package br.balladesh.pjcappbackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinIOEndpoint {
  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.rootuser}")
  private String accessKey;

  @Value("${minio.rootpassword}")
  private String secretKey;

  @Value("${minio.bucketname}")
  private String bucketName;

  public String getEndpoint() {
    return endpoint;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getBucketName() {
    return bucketName;
  }
}
