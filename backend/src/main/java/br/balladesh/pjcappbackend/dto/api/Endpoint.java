package br.balladesh.pjcappbackend.dto.api;

import br.balladesh.pjcappbackend.utilities.Defaults;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * A class to map an endpoint with the method to access that endpoint.
 */
public class Endpoint {
  private final String url;
  private final String method;

  public Endpoint() {
    this.url = Defaults.DEFAULT_STR;
    this.method = Defaults.DEFAULT_STR;
  }

  public Endpoint(String url, String method) {
    this.url = MoreObjects.firstNonNull(url, Defaults.DEFAULT_STR);
    this.method = MoreObjects.firstNonNull(method, Defaults.DEFAULT_STR);
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
    return Objects.hashCode(url, method);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("url", this.url)
        .add("method", this.method)
        .toString();
  }
}
