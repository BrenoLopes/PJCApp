package br.balladesh.pjcappbackend.utilities.errors;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class HttpExceptionTest {

  @Test
  void testGetStatusCode() {
    HttpStatus expected = HttpStatus.OK;
    HttpException exception = new HttpException(expected);

    assertSame(expected, exception.getStatusCode());
  }
}