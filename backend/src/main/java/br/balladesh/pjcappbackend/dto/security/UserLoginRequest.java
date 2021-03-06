package br.balladesh.pjcappbackend.dto.security;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * A DTO to map the clients request to signin a user with this credentials.
 */
public class UserLoginRequest {
  private final String username, password;

  public UserLoginRequest(String username, String password) {
    this.username = MoreObjects.firstNonNull(username, Defaults.DEFAULT_STR);
    this.password = MoreObjects.firstNonNull(password, Defaults.DEFAULT_STR);
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserLoginRequest)) return false;
    UserLoginRequest that = (UserLoginRequest) o;
    return username.equals(that.username)
        && password.equals(that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username, password);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("username", this.username)
        .add("password", this.password)
        .toString();
  }
}
