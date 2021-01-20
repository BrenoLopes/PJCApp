package br.balladesh.pjcappbackend.utilities.factories;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseCreator {
  public static ResponseEntity<MessageResponse> create(HttpStatus kind) {
    switch(kind) {
      case OK:
        return load("The request was successful.", kind);
      case UNAUTHORIZED:
        return load("You don't have permissions to access this page.", kind);
      case CONFLICT:
        return load("An entry already exist with the given information.", kind);
      case BAD_REQUEST:
        return load("A required parameter was not found or invalid.", kind);
      case FORBIDDEN:
        return load("This resource requires authentication to be accessed.", kind);
      case NOT_FOUND:
        return load("The requested item was not found.", kind);
      case INTERNAL_SERVER_ERROR:
        return load("The request couldn't be completed because an error happened in the server.", kind);
      default:
        return load(kind.getReasonPhrase(), kind);
    }
  }

  public static ResponseEntity<MessageResponse> create(String message, HttpStatus kind) {
    return load(message, kind);
  }

  private static ResponseEntity<MessageResponse> load(String message, HttpStatus kind) {
    return new ResponseEntity<>(new MessageResponse(message), kind);
  }
}
