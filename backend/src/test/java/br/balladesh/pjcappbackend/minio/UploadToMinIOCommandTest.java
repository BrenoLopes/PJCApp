package br.balladesh.pjcappbackend.minio;

import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class UploadToMinIOCommandTest {
  @Test
  void testWithNullConstructors() {
    UploadToMinIOCommand command = new UploadToMinIOCommand(null, null);
    Result<String, HttpException> result = command.execute();

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getException().getStatusCode());
  }
}