package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class MinIOService {
  private final MinIOEndpoint minIOEndpoint;
  private MinioClient client;

  @Autowired
  public MinIOService(MinIOEndpoint minIOEndpoint) {
    this.minIOEndpoint = minIOEndpoint;
    this.client = this.loadClient();
  }

  /**
   * Check if the requested file exists in minio and if true returns an url that will expire
   * in 5 minutes.
   *
   * @param fileName The filename
   * @throws NotFoundException if the file doesn't exist.
   * @throws InternalServerErrorException if an error happens in the process.
   *
   * @return url to the requested file
   */
  public String getFile(String fileName) throws NotFoundException {
    if (this.isFileNameEmpty(fileName))
      throw new NotFoundException("The requested file doesn't exist!");

    try {
      MinioClient minioClient = this.client;

      if (this.doesTheBucketNotExist(minioClient) || this.loadFileInformation(minioClient, fileName) == null)
        throw new NotFoundException("The requested file doesn't exist!");

      return this.createUrlToFile(minioClient, fileName);
    } catch(NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Upload the file to MinIO Bucket
   *
    * @param file Multipart file representing the file to be uploaded
   *
   * @throws BadRequestException if the file parameter is null
   * @throws InternalServerErrorException An error happens in the process.
   *
   * @return The name of the file inside the bucket
   */
  public String uploadFile(MultipartFile file) throws InternalServerErrorException, BadRequestException {
    if (file == null)
      throw new BadRequestException("The file cannot be null");

    try {
      MinioClient client = this.client;

      if (this.doesTheBucketNotExist(client))
        this.createTheBucket(client);

      String fileName = this.generateFileName(file.getOriginalFilename());

      this.uploadFileToMinIO(client, fileName, file);

      return fileName;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * Delete the file with this name from MinIO's bucket
   *
   * @param fileName the name of the file to be removed
   *
   * @throws NotFoundException if the file doesn't exist
   * @throws InternalServerErrorException if an error happens in the process
   */
  public void removeFile(String fileName) throws InternalServerErrorException, NotFoundException {
    try {
      MinioClient client = this.client;

      if (this.doesTheBucketNotExist(client) || this.loadFileInformation(client, fileName) == null)
        throw new NotFoundException("The file doesn't exist");

      this.deleteFileFromMinIO(client, fileName);
    } catch(NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }

  /**
   * This method is here to open this class to testing. It shouldn't be used by other
   * purposes.
   *
   * @param client MinIO Client
   */
  protected void setMinIOClient(MinioClient client) {
    this.client = client;
  }

  private MinioClient loadClient() {
    return MinioClient.builder()
        .endpoint(this.minIOEndpoint.getEndpoint())
        .credentials(this.minIOEndpoint.getAccessKey(), this.minIOEndpoint.getSecretKey())
        .build();
  }

  private boolean isFileNameEmpty(String fileName) {
    return fileName == null || fileName.equalsIgnoreCase("");
  }

  private boolean doesTheBucketNotExist(MinioClient client) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    return !client.bucketExists(
        BucketExistsArgs.builder().bucket(this.minIOEndpoint.getBucketName()).build()
    );
  }

  private void createTheBucket(MinioClient client) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    client.makeBucket(
        MakeBucketArgs.builder().bucket(this.minIOEndpoint.getBucketName()).build()
    );
  }

  private StatObjectResponse loadFileInformation(MinioClient client, String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    return client.statObject(
        StatObjectArgs.builder()
            .bucket(this.minIOEndpoint.getBucketName())
            .object(fileName)
            .build()
    );
  }

  private String createUrlToFile(MinioClient client, String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    return client.getPresignedObjectUrl(
        GetPresignedObjectUrlArgs.builder()
            .method(Method.GET)
            .bucket(this.minIOEndpoint.getBucketName())
            .object(fileName)
            .expiry(5, TimeUnit.MINUTES)
            .build()
    );
  }

  private String generateFileName(String originalFileName) {
    return String.format("%d-%s", System.currentTimeMillis(), originalFileName);
  }

  private void uploadFileToMinIO(MinioClient client, String fileName, MultipartFile file) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
    client.putObject(
        PutObjectArgs.builder()
            .bucket(this.minIOEndpoint.getBucketName())
            .object(fileName)
            .stream(file.getInputStream(), file.getSize(), -1)
            .contentType(file.getContentType())
            .build()
    );
  }

  private void deleteFileFromMinIO(MinioClient client, String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    client.removeObject(
        RemoveObjectArgs.builder()
            .bucket(this.minIOEndpoint.getBucketName())
            .object(fileName)
            .build()
    );
  }
}
