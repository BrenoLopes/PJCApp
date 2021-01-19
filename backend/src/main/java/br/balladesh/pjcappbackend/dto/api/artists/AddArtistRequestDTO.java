package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.utilities.Defaults;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class AddArtistRequestDTO {
  private final String name;

  public AddArtistRequestDTO(String name)
  {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddArtistRequestDTO that = (AddArtistRequestDTO) o;
    return Objects.equal(this.name, that.name);
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
