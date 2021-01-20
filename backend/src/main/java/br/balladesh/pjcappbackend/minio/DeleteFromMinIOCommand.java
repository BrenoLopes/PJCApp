package br.balladesh.pjcappbackend.minio;

import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.commands.Command;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteFromMinIOCommand implements Command<Boolean, HttpException> {
  private final MinIOEndpoint endpoint;
  private final String objectName;

  private final Logger logger = LoggerFactory.getLogger(DeleteFromMinIOCommand.class);

  public DeleteFromMinIOCommand(String objectName, MinIOEndpoint endpoint) {
    this.objectName = objectName;
    this.endpoint = endpoint;
  }

  @Override
  public Result<Boolean, HttpException> execute() {
    try {
      // Create a client connection to the server
      MinioClient minioClient = MinioClient.builder()
          .endpoint(this.endpoint.getEndpoint())
          .credentials(this.endpoint.getAccessKey(), this.endpoint.getSecretKey())
          .build();

      boolean found = minioClient
          .bucketExists(
              BucketExistsArgs.builder().bucket(this.endpoint.getBucketName()).build()
          );

      if (!found)
        return Result.from(false);

      // Try to remove the object from MinIO server
      minioClient.removeObject(
          RemoveObjectArgs
              .builder()
              .bucket(this.endpoint.getBucketName())
              .object(this.objectName)
              .build()
      );

      return Result.from(true);

    } catch (Exception e) {
      String message = "Couldn't delete a file to MinIO service. Cause: {}";
      logger.error(message, e.getMessage());
    }

    return Result.fromError(new InternalServerErrorException(""));
  }
}
