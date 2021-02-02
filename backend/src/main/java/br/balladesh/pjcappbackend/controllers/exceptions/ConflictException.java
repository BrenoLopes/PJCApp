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

  public ConflictException(String message, Throwable cause) {
    super(message, cause, HttpStatus.CONFLICT);
  }

  public ConflictException(Throwable cause) {
    super(cause, HttpStatus.CONFLICT);
  }

  public ConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, HttpStatus.CONFLICT);
  }
}
