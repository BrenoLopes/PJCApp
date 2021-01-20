package br.balladesh.pjcappbackend.minio;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.commands.Command;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploadToMinIOCommand implements Command<String, HttpException> {
  private final MultipartFile file;
  private final MinIOEndpoint endpoint;
  private final Logger logger = LoggerFactory.getLogger(UploadToMinIOCommand.class);

  private final String fileName;

  public UploadToMinIOCommand(MultipartFile file, MinIOEndpoint endpoint) {
    this.file = file;
    this.endpoint = endpoint;

    if (!file.isEmpty())
      this.fileName = String.format("%d-%s", System.currentTimeMillis(), this.file.getOriginalFilename());
    else
      this.fileName = "";
  }

  public String getFileName() {
     return this.fileName;
  }

  @Override
  public Result<String, HttpException> execute() {
    try {
      MinioClient minioClient = MinioClient.builder()
          .endpoint(this.endpoint.getEndpoint())
          .credentials(this.endpoint.getAccessKey(), this.endpoint.getSecretKey())
          .build();

      boolean found = minioClient
          .bucketExists(
              BucketExistsArgs.builder().bucket(this.endpoint.getBucketName()).build()
          );

      if (!found) {
        minioClient.makeBucket(
            MakeBucketArgs.builder().bucket(this.endpoint.getBucketName()).build()
        );
      }

      minioClient.putObject(
          PutObjectArgs
              .builder()
              .bucket(this.endpoint.getBucketName())
              .object(fileName)
              .stream(this.file.getInputStream(), this.file.getSize(), -1)
              .contentType(this.file.getContentType())
              .build()
      );

      return Result.from(fileName);
    } catch (Exception e) {
      String message = "Couldn't upload the file to MinIO service. Cause: {}";
      logger.error(message, e.getMessage());
    }

    return Result.fromError(new InternalServerErrorException("Could not process the request!"));
  }

}
