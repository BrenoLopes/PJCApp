package br.balladesh.pjcappbackend.dto.api.artists;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class PutArtistRequestDTO {
  private final String name;
  private final int id;

  public PutArtistRequestDTO(int id, String name) {
    this.name = MoreObjects.firstNonNull(name, "");
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PutArtistRequestDTO that = (PutArtistRequestDTO) o;
    return id == that.id && name.equals(that.name);
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
