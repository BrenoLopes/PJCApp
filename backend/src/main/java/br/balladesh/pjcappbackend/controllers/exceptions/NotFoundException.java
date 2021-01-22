package br.balladesh.pjcappbackend.controllers.exceptions;

import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {
  public NotFoundException() {
    super(HttpStatus.NOT_FOUND);
  }

  public NotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
