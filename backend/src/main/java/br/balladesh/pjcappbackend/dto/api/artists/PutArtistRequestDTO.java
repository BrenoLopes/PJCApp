package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class PutArtistRequestDTO {
  private final String name;
  private final long id;

  public PutArtistRequestDTO(long id, String name) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PutArtistRequestDTO that = (PutArtistRequestDTO) o;
    return id == that.id && Objects.equal(this.name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.name);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("name", this.name)
        .toString();
  }
}
