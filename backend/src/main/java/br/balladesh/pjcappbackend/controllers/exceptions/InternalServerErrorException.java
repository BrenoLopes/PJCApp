package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends HttpException {
  public InternalServerErrorException() {
    super(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerErrorException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
