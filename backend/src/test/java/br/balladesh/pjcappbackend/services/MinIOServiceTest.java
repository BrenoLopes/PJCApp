package br.balladesh.pjcappbackend.services;

import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
import br.balladesh.pjcappbackend.controllers.exceptions.BadRequestException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import br.balladesh.pjcappbackend.controllers.exceptions.NotFoundException;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application.test.properties")
class MinIOServiceTest {
  @Mock
  private MinioClient client;

  @Autowired
  private MinIOEndpoint endpoint;

  @Test
  public void notFoundWhenGettingFileWithEmptyFileName() {
    MinIOService testTarget = new MinIOService(this.endpoint);
    testTarget.setMinIOClient(client);

    Executable fn1 = () -> {
      testTarget.getFile("");
    };

    Executable fn2 = () -> {
      testTarget.getFile("");
    };

    assertThrows(NotFoundException.class, fn1);
    assertThrows(NotFoundException.class, fn2);
  }

  @Test
  public void notFoundWhenGettingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    Mockito.when(client.bucketExists(any())).thenReturn(true);
    Mockito.when(client.statObject(any())).thenReturn(null);

    MinIOService testTarget = new MinIOService(this.endpoint);
    testTarget.setMinIOClient(client);

    Executable fn = () -> {
      testTarget.getFile("Robocop");
    };

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void successWhenGettingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    String fileName = "Robocop";
    String expected = "robocop:///lets.say.this.is.a.valid.url";

    StatObjectResponse mockStat = mock(StatObjectResponse.class);

    Mockito.when(client.bucketExists(any())).thenReturn(true);
    Mockito.when(client.statObject(any())).thenReturn(mockStat);
    Mockito.when(client.getPresignedObjectUrl(any())).thenReturn(expected);

    MinIOService testTarget = new MinIOService(this.endpoint);
    testTarget.setMinIOClient(this.client);

    String received = testTarget.getFile(fileName);

    assertEquals(expected, received);
  }

  @Test
  public void internalServerErrorWhenGettingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    Mockito
        .lenient()
        .when(client.bucketExists(any()))
        .thenThrow(new ServerException("Whoops"));

    String fileName = "Robocop";
    MinIOService testTarget = new MinIOService(endpoint);

    Executable fn = () -> {
      testTarget.getFile(fileName);
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void internalServerErrorWhenUploadingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    Mockito.lenient().when(client.bucketExists(any())).thenReturn(true);
    Mockito.lenient().doThrow(new RuntimeException("Whoops")).when(client).makeBucket(any());

    String fileName = "Robocop";
    String originalFileName = fileName + ".png";
    MinIOService testTarget = new MinIOService(endpoint);

    Executable fn = () -> {
      testTarget.uploadFile(
          new MockMultipartFile(
              fileName, originalFileName, "image/png", new byte[0]
          )
      );
    };

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void badRequestUploadingFile() {
    MinIOService testTarget = new MinIOService(endpoint);

    Executable fn = () -> testTarget.uploadFile(null);

    assertThrows(BadRequestException.class, fn);
  }

  @Test
  public void successWhenUploadingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    ObjectWriteResponse mockResponse = mock(ObjectWriteResponse.class);

    Mockito.lenient().when(client.bucketExists(any())).thenReturn(true);
    Mockito.when(client.putObject(any())).thenReturn(mockResponse);

    String fileName = "Robocop";
    String originalFileName = fileName + ".png";

    MinIOService testTarget = new MinIOService(endpoint);
    testTarget.setMinIOClient(this.client);

    String resultFileName = testTarget.uploadFile(
        new MockMultipartFile(
            fileName, originalFileName, "image/png", new byte[0]
        )
    );

    assertNotNull(resultFileName);
    assertNotEquals("", resultFileName);
    assertTrue(resultFileName.contains(originalFileName));
  }

  @Test
  public void notFoundWhenRemovingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    Mockito.when(client.bucketExists(any())).thenReturn(true);
    Mockito.when(client.statObject(any())).thenReturn(null);

    MinIOService testTarget = new MinIOService(endpoint);
    testTarget.setMinIOClient(client);

    String fileName = "Robocop";

    Executable fn = () -> testTarget.removeFile(fileName);

    assertThrows(NotFoundException.class, fn);
  }

  @Test
  public void internalServerErrorWhenRemovingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    Mockito.when(client.bucketExists(any())).thenReturn(true);
    Mockito.when(client.statObject(any())).thenThrow(new RuntimeException("Whoops"));

    MinIOService testTarget = new MinIOService(endpoint);
    testTarget.setMinIOClient(client);

    String fileName = "Robocop";

    Executable fn = () -> testTarget.removeFile(fileName);

    assertThrows(InternalServerErrorException.class, fn);
  }

  @Test
  public void successWhenRemovingFile() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    StatObjectResponse mockStat = mock(StatObjectResponse.class);

    Mockito.when(client.bucketExists(any())).thenReturn(true);
    Mockito.when(client.statObject(any())).thenReturn(mockStat);
    Mockito.doNothing().when(client).removeObject(any());

    MinIOService testTarget = new MinIOService(endpoint);
    testTarget.setMinIOClient(client);

    String fileName = "Robocop";

    Executable fn = () -> testTarget.removeFile(fileName);

    assertDoesNotThrow(fn);
  }
}
