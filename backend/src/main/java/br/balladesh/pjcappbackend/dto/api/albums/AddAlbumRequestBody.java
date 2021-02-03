package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import br.balladesh.pjcappbackend.utilities.defaults.EmptyMultipartFile;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.web.multipart.MultipartFile;

public class AddAlbumRequestBody {
  private final String name;
  private final MultipartFile image;
  private final long artist_id;

  public AddAlbumRequestBody(String name, MultipartFile image, Long artist_id) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.image = MoreObjects.firstNonNull(image, new EmptyMultipartFile());
    this.artist_id = MoreObjects.firstNonNull(artist_id, Long.MIN_VALUE);
  }

  public String getName() {
    return name;
  }

  public MultipartFile getImage() {
    return image;
  }

  public long getArtistId() {
    return artist_id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddAlbumRequestBody that = (AddAlbumRequestBody) o;
    return artist_id == that.artist_id
        && name.equals(that.name)
        && image.equals(that.image);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.name, this.image, this.artist_id);
  }

  @JsonValue
  protected ObjectNode toJson() {
      ObjectMapper mapper = new ObjectMapper();
      ObjectNode node = mapper.createObjectNode();

      node.put("name", this.name);
      node.put("image", this.image.getOriginalFilename());
      node.put("artist_id", this.artist_id);

      return node;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", this.name)
        .add("image", this.image)
        .add("artist_id", this.artist_id)
        .toString();
  }
}
