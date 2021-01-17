package br.balladesh.pjcappbackend.dto;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class MessageResponse {
  private final String message;

  public MessageResponse(String message) {
    this.message = MoreObjects.firstNonNull(message, "");
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MessageResponse that = (MessageResponse) o;
    return message.equals(that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.message);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("message", this.message)
        .toString();
  }
}
