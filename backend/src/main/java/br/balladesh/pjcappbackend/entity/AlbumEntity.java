package br.balladesh.pjcappbackend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "id")
@Entity(name = "albums")
public class AlbumEntity {
  public AlbumEntity() {}

  public AlbumEntity(String name, ArtistEntity artist, String image) {
    this.name = name;
    this.image = image;
    this.artist = artist;
  }

  public AlbumEntity(Long id, String name, ArtistEntity artist, String image) {
    this.id = id;
    this.name = name;
    this.image = image;
    this.artist = artist;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String image;

  @ManyToOne
  private ArtistEntity artist;

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public ArtistEntity getArtist() {
    return artist;
  }

  public void setArtist(ArtistEntity artist) {
    this.artist = artist;
  }

  public void setId(long id) {
    this.id = id;
  }

  @JsonValue
  public ObjectNode toJson() {
    ObjectMapper mapper = new ObjectMapper();
    return this.toJsonObjectMapper(mapper);
  }

  @Override
  public String toString() {
    String str = "{"
        + "id=%d,"
        + "name=\"%s\","
        + "image=\"%s\","
        + "artist=%s,"
        + "}";

    return String.format(str, this.id, this.name, this.image, this.artist);
  }

  private ObjectNode toJsonObjectMapper(ObjectMapper mapper) {
    ObjectNode node = mapper.createObjectNode();
    node.put("id", this.id);
    node.put("name", this.name);
    node.put("image", this.image);
    node.put("artistName", this.artist.getName());

    return node;
  }
}
