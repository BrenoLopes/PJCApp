package br.balladesh.pjcappbackend.dto.api.artists;

import java.util.Objects;

public class PutArtistRequestDTO {
  private String name;
  private int id;

  public PutArtistRequestDTO() {
    this.name = "";
    this.id = Integer.MIN_VALUE;
  }

  public PutArtistRequestDTO(String name, int id) {
    this.name = name == null ? "" : name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name == null ? "" : name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
    return Objects.hash(name, id);
  }

  @Override
  public String toString() {
    String fmt = "{"
        + "id=%d,"
        + "name=\"%s\","
        + "}";

    return String.format(fmt, this.id, this.name);
  }
}
