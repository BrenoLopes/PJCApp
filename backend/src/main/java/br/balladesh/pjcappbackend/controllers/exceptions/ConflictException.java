package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {
  public ConflictException() {
    super(HttpStatus.CONFLICT);
  }

  public ConflictException(String message) {
    super(message, HttpStatus.CONFLICT);
  }
}
