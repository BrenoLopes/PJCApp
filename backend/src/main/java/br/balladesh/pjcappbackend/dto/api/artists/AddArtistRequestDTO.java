package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class AddArtistRequestDTO {
  private final String name;

  public AddArtistRequestDTO() {
    this(Defaults.DEFAULT_STR);
  }

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

  @JsonValue
  public ObjectNode toJson() {
    ObjectMapper mapper = new ObjectMapper();

    ObjectNode node = mapper.createObjectNode();
    node.put("name", this.name);

    return node;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", this.name)
        .toString();
  }
}
