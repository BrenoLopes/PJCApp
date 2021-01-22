package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ConflictExceptionTest {
  @Test
  void testGettingHttpStatusFromException() {
    try {
      throw new ConflictException();
    } catch (HttpException e) {
      assertSame(HttpStatus.CONFLICT, e.getStatusCode());
    }
  }

  @Test
  void testGettingMessageFromHttpStatusFromException() {
    String message = "Testing1234";
    try {
      throw new ConflictException(message);
    } catch (HttpException e) {
      assertSame(HttpStatus.CONFLICT, e.getStatusCode());
      assertEquals(message, e.getMessage());
    }
  }
}