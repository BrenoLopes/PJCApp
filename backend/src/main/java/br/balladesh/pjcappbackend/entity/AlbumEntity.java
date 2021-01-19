package br.balladesh.pjcappbackend.entity;

import br.balladesh.pjcappbackend.utilities.Defaults;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "id")
@Entity(name = "albums")
public class AlbumEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String image;

  @ManyToOne
  private ArtistEntity artist;

  public AlbumEntity() {
    this.name = Defaults.DEFAULT_STR;
    this.image = Defaults.DEFAULT_STR;
    this.id = Defaults.getDefaultInt();
    this.artist = new ArtistEntity();
  }

  public AlbumEntity(String name, ArtistEntity artist, String image) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.image = MoreObjects.firstNonNull(image, Defaults.DEFAULT_STR);
    this.artist = MoreObjects.firstNonNull(artist, new ArtistEntity());
  }

  public AlbumEntity(Long id, String name, ArtistEntity artist, String image) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.image = MoreObjects.firstNonNull(image, Defaults.DEFAULT_STR);
    this.artist = MoreObjects.firstNonNull(artist, new ArtistEntity());
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = MoreObjects.firstNonNull(image, Defaults.DEFAULT_STR);
  }

  public ArtistEntity getArtist() {
    return artist;
  }

  public void setArtist(ArtistEntity artist) {
    this.artist = MoreObjects.firstNonNull(artist, new ArtistEntity());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AlbumEntity that = (AlbumEntity) o;
    return id == that.id
        && Objects.equal(this.name, that.name)
        && Objects.equal(this.image, that.image)
        && Objects.equal(this.artist, that.artist);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, image, artist);
  }

  @JsonValue
  public ObjectNode toJson() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    node.put("id", this.id);
    node.put("name", this.name);
    node.put("image", this.image);
    node.put("artistName", this.artist.getName());

    return node;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("name", this.name)
        .add("image", this.image)
        .add("artist", this.artist)
        .toString();
  }
}
