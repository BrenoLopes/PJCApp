package br.balladesh.pjcappbackend.dto.api.artists;

import java.util.Objects;

public class AddArtistRequestDTO {
  private String name;

  public AddArtistRequestDTO() {
    this.name = "";
  }

  public AddArtistRequestDTO(String name) {
    this.name = name == null ? "" : name;
  }

  public void setName(String name) {
    this.name = name == null ? "" : name;
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
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    String fmt = "{"
        + "name=\"%s\","
        + "}";

    return String.format(fmt, this.name);
  }
}
