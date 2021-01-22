package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class InternalServerErrorExceptionTest {
  @Test
  void testGettingHttpStatusFromException() {
    try {
      throw new InternalServerErrorException();
    } catch (HttpException e) {
      assertSame(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
    }
  }

  @Test
  void testGettingMessageFromHttpStatusFromException() {
    String message = "Testing1234";
    try {
      throw new InternalServerErrorException(message);
    } catch (HttpException e) {
      assertSame(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
      assertEquals(message, e.getMessage());
    }
  }
}