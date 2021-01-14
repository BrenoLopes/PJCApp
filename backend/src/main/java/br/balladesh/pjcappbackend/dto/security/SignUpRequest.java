package br.balladesh.pjcappbackend.dto.security;

import java.io.Serializable;
import java.util.Objects;

public class SignUpRequest implements Serializable {
  private final static long serialVersionUID = 4398430684586495835L;

  private final String username, name, password;

  public SignUpRequest(String username, String name, String password) {
    this.username = username;
    this.name = name;
    this.password = password;
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
    if (!(o instanceof SignUpRequest)) return false;
    SignUpRequest that = (SignUpRequest) o;
    return username.equals(that.username) && name.equals(that.name) && password.equals(that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, name, password);
  }

  @Override
  public String toString() {
    String str = "{"
        + "name=\"%s\","
        + "username=\"%s\","
        + "password=\"%s\""
        + "}";

    return String.format(
        str,
        this.name,
        this.username,
        this.password
    );
  }
}
