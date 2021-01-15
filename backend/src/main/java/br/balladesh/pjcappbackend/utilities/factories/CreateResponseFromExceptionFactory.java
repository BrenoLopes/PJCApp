package br.balladesh.pjcappbackend.utilities.factories;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.controllers.exceptions.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class CreateResponseFromExceptionFactory implements Factory<ResponseEntity<MessageResponse>, HttpException> {

  private static final Logger logger = LoggerFactory.getLogger(CreateResponseFromExceptionFactory.class);
  private HttpException exception;

  public CreateResponseFromExceptionFactory(HttpException e) {
    this.exception = e;
  }

  @Override
  public Result<ResponseEntity<MessageResponse>, HttpException> create() {
    if (this.exception == null) {
      logger.error("BuildResponseFromException::build Exception is null.");
      this.exception = new InternalServerErrorException("");
    }

    MessageResponse response = new MessageResponse(this.exception.getMessage());
    return Result.from(new ResponseEntity<>(response, this.exception.getStatusCode()));
  }
}
