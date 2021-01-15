package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends HttpException {
  public InternalServerErrorException() {
    super(
        "An error occurred when trying to process your request!",
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  public InternalServerErrorException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerErrorException(String message, Throwable cause) {
    super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerErrorException(Throwable cause) {
    super(cause, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
