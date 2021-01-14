package br.balladesh.pjcappbackend.dto.security;

import java.util.Objects;

public class JsonResponse {

  private final String username, token;

  public JsonResponse(String username, String token) {
    if (username == null)
      this.username = "";
    else
      this.username = username;


    if (token == null)
      this.token = "";
    else
      this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public String getToken() {
    return token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JsonResponse that = (JsonResponse) o;
    return username.equals(that.username) && token.equals(that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, token);
  }

  @Override
  public String toString() {
    String str = "{"
        + "username=\"%s\","
        + "token=\"%s\""
        + "}";

    return String.format(
        str,
        this.username,
        this.token
    );
  }
}
