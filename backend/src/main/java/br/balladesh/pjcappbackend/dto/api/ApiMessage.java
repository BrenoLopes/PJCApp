package br.balladesh.pjcappbackend.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class ApiMessage {
  private final String message;

  @JsonProperty("api.login")
  private final Endpoint api_login;

  @JsonProperty("api.signup")
  private final Endpoint api_signup;

  @JsonProperty("api.refresh")
  private final Endpoint api_refresh;

  public ApiMessage(String message, Endpoint apiLogin, Endpoint apiSignup, Endpoint apiRefresh) {
    this.message = message;
    this.api_login = apiLogin;
    this.api_signup = apiSignup;
    this.api_refresh = apiRefresh;
  }

  public String getMessage() {
    return message;
  }

  public Endpoint getApi_login() {
    return api_login;
  }

  public Endpoint getApi_signup() {
    return api_signup;
  }

  public Endpoint getApi_refresh() {
    return api_refresh;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ApiMessage that = (ApiMessage) o;
    return message.equals(that.message)
        && api_login.equals(that.api_login)
        && api_signup.equals(that.api_signup)
        && api_refresh.equals(that.api_refresh);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, api_login, api_signup, api_refresh);
  }

  @Override
  public String toString() {
    String str = "{"
        + "message=\"%s\","
        + "api.login=%s,"
        + "api.signup=%s,"
        + "api.refresh=%s"
        + "}";

    return String.format(
        str,
        this.message,
        this.api_login.toString(),
        this.api_signup.toString(),
        this.api_refresh.toString()
    );
  }
}
