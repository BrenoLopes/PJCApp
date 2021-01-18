package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.utilities.Defaults;
import br.balladesh.pjcappbackend.utilities.defaults.EmptyMultipartFile;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.web.multipart.MultipartFile;

public class EditAlbumRequestBody {
  private final String name;
  private final long id, artistId;
  private final MultipartFile image;

  public EditAlbumRequestBody(Long id, String name, MultipartFile image, Long artistId) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.image = MoreObjects.firstNonNull(image, new EmptyMultipartFile());
    this.artistId = MoreObjects.firstNonNull(artistId, Defaults.getDefaultLong());
  }

  public String getName() {
    return name;
  }

  public MultipartFile getImage() {
    return image;
  }

  public long getAlbumId() {
    return this.id;
  }

  public long getArtistId() {
    return artistId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EditAlbumRequestBody that = (EditAlbumRequestBody) o;
    return id == that.id
        && artistId == that.artistId
        && name.equals(that.name)
        && image.equals(that.image);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, image, id, artistId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("name", this.name)
        .add("image", this.image)
        .add("artistId", this.artistId)
        .toString();
  }
}

