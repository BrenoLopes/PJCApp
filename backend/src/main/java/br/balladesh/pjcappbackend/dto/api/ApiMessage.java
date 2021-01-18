package br.balladesh.pjcappbackend.dto.api;

import br.balladesh.pjcappbackend.utilities.Defaults;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

/**
 * A DTO class to show a welcome message at the api endpoint for the client to have more information about the
 * server.
 */
public class ApiMessage {
  private final String message;

  private final ImmutableMap<String, Endpoint> api_security;

  private final ImmutableMap<String, Endpoint> api_artists;

  private final ImmutableMap<String, Endpoint> api_albums;

  public ApiMessage(
      String message,
      ImmutableMap<String, Endpoint> securityEndpoints,
      ImmutableMap<String, Endpoint> artistsEndpoints,
      ImmutableMap<String, Endpoint> albumsEndpoints
  ) {
    this.message = MoreObjects.firstNonNull(message, Defaults.DEFAULT_STR);
    this.api_security = MoreObjects.firstNonNull(securityEndpoints, ImmutableMap.of());
    this.api_artists = MoreObjects.firstNonNull(artistsEndpoints, ImmutableMap.of());
    this.api_albums = MoreObjects.firstNonNull(albumsEndpoints, ImmutableMap.of());
  }

  public String getMessage() {
    return message;
  }

  @JsonProperty("api.security")
  public ImmutableMap<String, Endpoint> getApiSecurityEndpoints() {
    return this.api_security;
  }

  @JsonProperty("api.artists")
  public ImmutableMap<String, Endpoint> getApiArtistsEndpoints() {
    return this.api_artists;
  }

  @JsonProperty("api.albums")
  public ImmutableMap<String, Endpoint> getApiAlbumsEndpoints() {
    return this.api_albums;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ApiMessage that = (ApiMessage) o;
    return message.equals(that.message)
        && api_security.equals(that.api_security)
        && api_artists.equals(that.api_artists)
        && api_albums.equals(that.api_albums);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.message, this.api_security, this.api_artists, this.api_albums);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("message", this.message)
        .add("api.security", this.api_security)
        .add("api.artists", this.api_artists)
        .add("api.albums", this.api_albums)
        .toString();
  }
}
