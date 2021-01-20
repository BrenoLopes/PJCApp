package br.balladesh.pjcappbackend.utilities.factories;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ResponseCreatorTest {

  @Test
  public void createOk() {
    HttpStatus status = HttpStatus.OK;

    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse("The request was successful."),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomSuccessOK";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }

  @Test
  public void createUnauthorized() {
    HttpStatus status = HttpStatus.UNAUTHORIZED;

    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse("You don't have permissions to access this page."),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomSuccessUnauthorized";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }

  @Test
  public void createConflict() {
    HttpStatus status = HttpStatus.CONFLICT;
    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse("An entry already exist with the given information."),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomConflict";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }

  @Test
  public void createBadRequest() {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse("A required parameter was not found or invalid."),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomBadRequest";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }

  @Test
  public void createForbidden() {
    HttpStatus status = HttpStatus.FORBIDDEN;
    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse("This resource requires authentication to be accessed."),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomForbidden";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }

  @Test
  public void createNotFound() {
    HttpStatus status = HttpStatus.NOT_FOUND;
    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse("The requested item was not found."),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomNotFound";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }

  @Test
  public void createInternalServerError() {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse("The request couldn't be completed because an error happened in the server."),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomInternalServerError";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }

  @Test
  public void createGeneric() {
    HttpStatus status = HttpStatus.ACCEPTED;
    ResponseEntity<MessageResponse> responseWithoutMessage = ResponseCreator.create(status);
    ResponseEntity<MessageResponse> expected = new ResponseEntity<>(
        new MessageResponse(status.getReasonPhrase()),
        status
    );

    assertEquals(responseWithoutMessage, expected);

    String message = "RandomAccepted";
    ResponseEntity<MessageResponse> responseWithMessage = ResponseCreator.create(message, status);
    expected = new ResponseEntity<>(new MessageResponse(message), status);

    assertEquals(responseWithMessage, expected);
  }
}