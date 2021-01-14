package br.balladesh.pjcappbackend.controllers;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BuildResponseFromException implements Builder<ResponseEntity<MessageResponse>, HttpException>{
  private static final Logger logger = LoggerFactory.getLogger(BuildResponseFromException.class);

  private HttpException exception;

  public BuildResponseFromException(HttpException e) {
    this.exception = e;
  }

  @Override
  public Result<ResponseEntity<MessageResponse>, HttpException> build() {
    if (this.exception == null) {
      logger.error("BuildResponseFromException::build Exception is null.");
      this.exception = new InternalServerErrorException("");
    }

    MessageResponse response = new MessageResponse(this.exception.getMessage());

    if (this.exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
      return Result.from(new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR));
    }
    if (this.exception.getStatusCode() ==  HttpStatus.BAD_REQUEST) {
      return Result.from(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }

    return Result.from(new ResponseEntity<>(response, HttpStatus.NOT_IMPLEMENTED));
  }
}
