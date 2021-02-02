package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {
  public UnauthorizedException() {
    super(HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedException(String message, Throwable cause) {
    super(message, cause, HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedException(Throwable cause) {
    super(cause, HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, HttpStatus.UNAUTHORIZED);
  }
}
