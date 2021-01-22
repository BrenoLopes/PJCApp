package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class UnauthorizedCredentialsException extends HttpException {
  public UnauthorizedCredentialsException() {
    super(HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedCredentialsException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
