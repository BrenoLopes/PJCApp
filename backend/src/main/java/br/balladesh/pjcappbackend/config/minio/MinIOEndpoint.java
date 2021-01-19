package br.balladesh.pjcappbackend.config.minio;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class MinIOEndpoint {
  private final String endpoint, accessKey, secretKey, bucketName;

  public MinIOEndpoint(String endpoint, String accessKey, String secretKey, String bucketName) {
    this.endpoint = MoreObjects.firstNonNull(endpoint, Defaults.DEFAULT_STR);
    this.accessKey = MoreObjects.firstNonNull(accessKey, Defaults.DEFAULT_STR);
    this.secretKey = MoreObjects.firstNonNull(secretKey, Defaults.DEFAULT_STR);
    this.bucketName = MoreObjects.firstNonNull(bucketName, Defaults.DEFAULT_STR);
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MinIOEndpoint that = (MinIOEndpoint) o;
    return Objects.equal(this.endpoint, that.endpoint)
        && Objects.equal(this.accessKey, that.accessKey)
        && Objects.equal(this.secretKey, that.secretKey)
        && Objects.equal(this.bucketName, that.bucketName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(endpoint, accessKey, secretKey, bucketName);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("accessKey", this.accessKey)
        .add("secretKey", this.secretKey)
        .add("bucketName", this.bucketName)
        .toString();
  }
}
