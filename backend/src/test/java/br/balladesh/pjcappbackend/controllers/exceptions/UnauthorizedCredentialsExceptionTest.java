package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedCredentialsExceptionTest {
  @Test
  void testGettingHttpStatusFromException() {
    try {
      throw new UnauthorizedCredentialsException();
    } catch (HttpException e) {
      assertSame(HttpStatus.UNAUTHORIZED, e.getStatusCode());
    }
  }

  @Test
  void testGettingMessageFromHttpStatusFromException() {
    String message = "Testing1234";
    try {
      throw new UnauthorizedCredentialsException(message);
    } catch (HttpException e) {
      assertSame(HttpStatus.UNAUTHORIZED, e.getStatusCode());
      assertEquals(message, e.getMessage());
    }
  }
}