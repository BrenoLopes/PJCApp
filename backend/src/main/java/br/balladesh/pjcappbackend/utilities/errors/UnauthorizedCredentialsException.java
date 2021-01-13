package br.balladesh.pjcappbackend.utilities.errors;

import org.springframework.http.HttpStatus;

public class UnauthorizedCredentialsException extends HttpException {
  public UnauthorizedCredentialsException() {
    super(HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedCredentialsException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedCredentialsException(String message, Throwable cause) {
    super(message, cause, HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedCredentialsException(Throwable cause) {
    super(cause, HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, HttpStatus.UNAUTHORIZED);
  }
}
