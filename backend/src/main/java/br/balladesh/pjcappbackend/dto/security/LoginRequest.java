package br.balladesh.pjcappbackend.dto.security;

import java.io.Serializable;
import java.util.Objects;

public class LoginRequest implements Serializable {
  private final static long serialVersionUID = 1239813182084128498L;

  private final String username, password;

  public LoginRequest(String username, String password) {
    this.username = username;
    this.password = password;
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
    if (!(o instanceof LoginRequest)) return false;
    LoginRequest that = (LoginRequest) o;
    return username.equals(that.username)
        && password.equals(that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }

  @Override
  public String toString() {
    String str = "{"
        + "username=\"%s\","
        + "password=\"%s\","
        + "}";

    return String.format(
        str,
        this.username,
        this.password
    );
  }
}
