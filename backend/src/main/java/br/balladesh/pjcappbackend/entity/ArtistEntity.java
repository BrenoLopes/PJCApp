package br.balladesh.pjcappbackend.entity;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity(name = "artists")
public class ArtistEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "artist")
  private final List<AlbumEntity> albums;

  @ManyToOne
  @JoinColumn(name = "author")
  private final UserEntity createdByUser;

  public ArtistEntity() {
    this.name = Defaults.DEFAULT_STR;
    this.albums = Lists.newArrayList();
    this.createdByUser = new UserEntity();
  }

  public ArtistEntity(String name, List<AlbumEntity> album, UserEntity createdByUser) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.albums = MoreObjects.firstNonNull(album, Lists.newArrayList());
    this.createdByUser = MoreObjects.firstNonNull(createdByUser, new UserEntity());
  }

  public ArtistEntity(Long id, String name, List<AlbumEntity> album, UserEntity createdByUser) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.albums = MoreObjects.firstNonNull(album, Lists.newArrayList());
    this.createdByUser = MoreObjects.firstNonNull(createdByUser, new UserEntity());
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
  }

  public List<AlbumEntity> getAlbums() {
      return albums;
  }

  public UserEntity getCreatedByUser() {
    return createdByUser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArtistEntity that = (ArtistEntity) o;
    return id == that.id
        && Objects.equal(this.name, that.name)
        && Objects.equal(this.albums, that.albums)
        && Objects.equal(this.createdByUser, that.createdByUser);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, albums);
  }

  @JsonValue
  public JsonNode toJson() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    node.put("id", this.id);
    node.put("name", this.name);

    ArrayNode albums = mapper.valueToTree(this.albums);
    node.putArray("albums").addAll(albums);

    node.put("created_by", this.createdByUser.getName());

    return node;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("name", this.name)
        .add("albums", this.albums)
        .add("created_by", this.createdByUser)
        .toString();
  }
}
