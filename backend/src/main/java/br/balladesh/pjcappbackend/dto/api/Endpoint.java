package br.balladesh.pjcappbackend.dto.api;

import java.util.Objects;

public class Endpoint {
  private final String url;
  private final String method;

  public Endpoint(String url, String method) {
    this.url = url;
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public String getMethod() {
    return method;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Endpoint endpoint = (Endpoint) o;
    return url.equals(endpoint.url) && method.equals(endpoint.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, method);
  }

  @Override
  public String toString() {
    String str = "{"
        + "url=\"%s\","
        + "method=\"%s\""
        + "}";

    return String.format(
        str,
        this.url,
        this.method
    );
  }
}
