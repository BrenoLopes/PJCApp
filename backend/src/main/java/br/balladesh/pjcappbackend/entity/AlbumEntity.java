package br.balladesh.pjcappbackend.entity;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
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

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private ArtistEntity artist;

  public AlbumEntity() {
    this(Defaults.getDefaultLong(), Defaults.DEFAULT_STR, new ArtistEntity(), Defaults.DEFAULT_STR);
  }

  public AlbumEntity(String name, String image) {
    this(name, new ArtistEntity(), image);
  }

  public AlbumEntity(String name, ArtistEntity artist, String image) {
    this(Defaults.getDefaultLong(), name, artist, image);
  }

  public AlbumEntity(Long id, String name, ArtistEntity artist, String image) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.image = MoreObjects.firstNonNull(image, Defaults.DEFAULT_STR);
    this.artist = MoreObjects.firstNonNull(artist, new ArtistEntity());
  }


  // Getters
  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public ArtistEntity getArtist() {
    return artist;
  }


  // Setters
  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setArtist(ArtistEntity artist) {
    this.setArtist(artist, true);
  }

  void setArtist(ArtistEntity artist, boolean shouldAdd) {
    this.artist = artist;

    if (artist != null && shouldAdd) {
      artist.addAlbum(this, false);
    }
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
