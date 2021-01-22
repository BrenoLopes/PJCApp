package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {
  @Test
  void testGettingHttpStatusFromException() {
    try {
      throw new NotFoundException();
    } catch (HttpException e) {
      assertSame(HttpStatus.NOT_FOUND, e.getStatusCode());
    }
  }

  @Test
  void testGettingMessageFromHttpStatusFromException() {
    String message = "Testing1234";
    try {
      throw new NotFoundException(message);
    } catch (HttpException e) {
      assertSame(HttpStatus.NOT_FOUND, e.getStatusCode());
      assertEquals(message, e.getMessage());
    }
  }
}