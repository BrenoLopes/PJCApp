package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {
  public BadRequestException() {
    super(HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
