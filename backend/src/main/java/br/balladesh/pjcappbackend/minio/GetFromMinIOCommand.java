package br.balladesh.pjcappbackend.minio;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.commands.Command;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.predicates.HasNull;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GetFromMinIOCommand implements Command<String, HttpException> {
  private final String file;
  private final MinIOEndpoint endpoint;
  private final Logger logger = LoggerFactory.getLogger(UploadToMinIOCommand.class);

  public GetFromMinIOCommand(String file, MinIOEndpoint endpoint) {
    this.file = file == null ? "" : file;
    this.endpoint = endpoint;
  }

  @Override
  public Result<String, HttpException> execute() {
    if(HasNull.withParams(this.endpoint, this.file).check()) {
      this.logger.error("GetFromMinIOCommand::execute Endpoint or the object name is null!");
      return Result.fromError(new InternalServerErrorException());
    }

    try {
      if (this.file.equals(""))
        return this.returnEmptyUrl();

      MinioClient minioClient = MinioClient.builder()
          .endpoint(this.endpoint.getEndpoint())
          .credentials(this.endpoint.getAccessKey(), this.endpoint.getSecretKey())
          .build();

      boolean found = minioClient
          .bucketExists(
              BucketExistsArgs.builder().bucket(this.endpoint.getBucketName()).build()
          );

      Object object = minioClient.statObject(
          StatObjectArgs.builder()
              .bucket(this.endpoint.getBucketName())
              .object(this.file)
              .build()
      );

      if (!found && object != null)
        return Result.from("");

      String url =
          minioClient.getPresignedObjectUrl(
              GetPresignedObjectUrlArgs.builder()
                  .method(Method.GET)
                  .bucket(this.endpoint.getBucketName())
                  .object(this.file)
                  .expiry(5, TimeUnit.MINUTES)
                  .build());

      return Result.from(url);
    } catch (Exception e) {
      logger.error(
          "GetFromMinIOCommand::execute Could not load the url for object {}. Error: {}",
          this.file,
          e.getMessage()
      );
    }

    return this.returnEmptyUrl();
  }

  private Result<String, HttpException> returnEmptyUrl() {
    return Result.from("");
  }
}
