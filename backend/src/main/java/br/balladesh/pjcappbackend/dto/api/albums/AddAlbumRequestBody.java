package br.balladesh.pjcappbackend.dto.api.albums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

public class AddAlbumRequestBody {
  private String name;
  private MultipartFile image;
  private Optional<String> artist_name;
  private Optional<Long> artist_id;

  public AddAlbumRequestBody(String name, MultipartFile image, Optional<Long> artist_id, Optional<String> artist_name) {
    this.name = name;
    this.image = image;
    this.artist_id = artist_id;
    this.artist_name = artist_name;
  }

  public String getName() {
    return name;
  }

  public MultipartFile getImage() {
    return image;
  }

  public Optional<String> getArtistName() {
    return artist_name;
  }

  public Optional<Long> getArtistId() {
    return artist_id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddAlbumRequestBody that = (AddAlbumRequestBody) o;
    return name.equals(that.name) && image.equals(that.image) && Objects.equals(artist_name, that.artist_name) && Objects.equals(artist_id, that.artist_id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, image, artist_name, artist_id);
  }

  @JsonValue
  protected ObjectNode toJson() {
      ObjectMapper mapper = new ObjectMapper();
      ObjectNode node = mapper.createObjectNode();

      node.put("name", this.name);
      node.put("image", this.image.getOriginalFilename());

      String name = this.artist_name.orElse("");
      node.put("artist_name", name);

      long id = this.artist_id.orElse(-1L);
      node.put("artist_id", id);

      return node;
  }
}
