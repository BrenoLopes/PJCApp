package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {
  public BadRequestException() {
    super("The request could not be processed, because the parameters are invalid!", HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(Throwable cause) {
    super(cause, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, HttpStatus.BAD_REQUEST);
  }
}
