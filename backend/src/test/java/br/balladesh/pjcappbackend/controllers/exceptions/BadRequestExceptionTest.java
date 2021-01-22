package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {
  @Test
  void testGettingHttpStatusFromException() {
    try {
      throw new BadRequestException();
    } catch (HttpException e) {
      assertSame(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }
  }

  @Test
  void testGettingMessageFromHttpStatusFromException() {
    String message = "Testing1234";
    try {
      throw new BadRequestException(message);
    } catch (HttpException e) {
      assertSame(HttpStatus.BAD_REQUEST, e.getStatusCode());
      assertEquals(message, e.getMessage());
    }
  }
}