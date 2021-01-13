package br.balladesh.pjcappbackend.dto;

import java.util.Objects;

public class MessageResponse {
  private final String message;

  public MessageResponse(String message) {
    if (message == null)
      this.message = "";
    else
      this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MessageResponse that = (MessageResponse) o;
    return Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message);
  }

  @Override
  public String toString() {
    String str = "{"
        + "message=\"%s\""
        + "}";

    return String.format(
        str,
        this.message
    );
  }
}
