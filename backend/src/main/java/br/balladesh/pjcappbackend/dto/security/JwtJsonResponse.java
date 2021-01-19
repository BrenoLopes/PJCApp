package br.balladesh.pjcappbackend.dto.security;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * A DTO class to map the backend response to the client at the clients request to refresh their jwt token with
 * an unexpired and valid token.
 */
public class JwtJsonResponse {
  private final String username, token;

  public JwtJsonResponse(String username, String token) {
    this.username = MoreObjects.firstNonNull(username, Defaults.DEFAULT_STR);
    this.token = MoreObjects.firstNonNull(token, Defaults.DEFAULT_STR);
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
    JwtJsonResponse that = (JwtJsonResponse) o;
    return username.equals(that.username) && token.equals(that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.username, this.token);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("username", this.username)
        .add("token", this.token)
        .toString();
  }
}
