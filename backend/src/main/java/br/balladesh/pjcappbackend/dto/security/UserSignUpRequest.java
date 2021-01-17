package br.balladesh.pjcappbackend.dto.security;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * A DTO class to map a clients json, representing the clients request to signup on the system with this
 * credentials.
 */
public class UserSignUpRequest {
  private final String username, name, password;

  public UserSignUpRequest(String username, String name, String password) {
    this.username = MoreObjects.firstNonNull(username, "");
    this.name = MoreObjects.firstNonNull(name, "");
    this.password = MoreObjects.firstNonNull(password, "");
  }

  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserSignUpRequest)) return false;
    UserSignUpRequest that = (UserSignUpRequest) o;
    return username.equals(that.username) && name.equals(that.name) && password.equals(that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username, name, password);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", this.name)
        .add("username", this.username)
        .add(password, this.password)
        .toString();
  }
}
