package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {
  public ForbiddenException() {
    super(HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause, HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(Throwable cause) {
    super(cause, HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, HttpStatus.FORBIDDEN);
  }
}
