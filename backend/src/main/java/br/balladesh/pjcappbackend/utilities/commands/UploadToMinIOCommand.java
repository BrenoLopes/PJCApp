package br.balladesh.pjcappbackend.utilities.commands;

import br.balladesh.pjcappbackend.services.MinIOEndpoint;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploadToMinIOCommand implements Command<String, HttpException>{
  private final MultipartFile file;
  private final MinIOEndpoint endpoint;
  private final Logger logger = LoggerFactory.getLogger(UploadToMinIOCommand.class);

  public UploadToMinIOCommand(MultipartFile file, MinIOEndpoint endpoint) {
    this.file = file;
    this.endpoint = endpoint;
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

      String fileName = String.format("%d-%s", System.currentTimeMillis(), this.file.getOriginalFilename());

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
