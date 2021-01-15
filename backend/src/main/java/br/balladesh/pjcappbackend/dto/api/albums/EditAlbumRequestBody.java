package br.balladesh.pjcappbackend.dto.api.albums;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;
import java.util.Optional;

public class EditAlbumRequestBody {
  private final String name;
  private final long id, artist_id;
  private final MultipartFile image;

  public EditAlbumRequestBody(long id, Optional<String> name, Optional<MultipartFile> image, Optional<Long> artist_id) {
    this.id = id;
    this.name = name.orElse("");
    this.image = image.orElse(new EmptyMultipartFile() );
    this.artist_id = artist_id.orElse(Long.MIN_VALUE);
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
    return artist_id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EditAlbumRequestBody that = (EditAlbumRequestBody) o;
    return this.id == that.id
        && artist_id == that.artist_id
        && Objects.equals(name, that.name)
        && Objects.equals(image, that.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, image, id, artist_id);
  }

  @Override
  public String toString() {
    String str
        = "{"
        + "id=\"%d\","
        + "name=%s,"
        + "image=\"%s\","
        + "artist_id=\"%d\""
        + "}";

    return String.format(
        str,
        this.id,
        this.name,
        this.image,
        this.artist_id
    );
  }
}

final class EmptyMultipartFile implements MultipartFile
{
  @Override
  public String getName() {
    return "";
  }

  @Override
  public String getOriginalFilename() {
    return "";
  }

  @Override
  public String getContentType() {
    return "";
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public long getSize() {
    return 0;
  }

  @Override
  public byte[] getBytes() throws IOException {
    return new byte[0];
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(this.getBytes());
  }

  @Override
  public void transferTo(File file) throws IOException, IllegalStateException {}
}