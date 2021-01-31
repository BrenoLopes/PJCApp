package br.balladesh.pjcappbackend.utilities.errors;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
  private final HttpStatus statusCode;

  public HttpException(HttpStatus statusCode) {
    this.statusCode = statusCode;
  }

  public HttpException(String message, HttpStatus statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public HttpException(String message, Throwable cause, HttpStatus statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public HttpException(Throwable cause, HttpStatus statusCode) {
    super(cause);
    this.statusCode = statusCode;
  }

  public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus statusCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.statusCode = statusCode;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }
}
