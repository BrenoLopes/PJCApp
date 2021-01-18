package br.balladesh.pjcappbackend.dto.api.artists;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class AddArtistRequestDTO {
  private final String name;

  public AddArtistRequestDTO(String name)
  {
    this.name = MoreObjects.firstNonNull(name, "");
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddArtistRequestDTO that = (AddArtistRequestDTO) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.name);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", this.name)
        .toString();
  }
}
