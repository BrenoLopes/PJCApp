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
import com.google.common.collect.ImmutableList;
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

  @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "artist")
  private List<AlbumEntity> albums;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "author")
  private UserEntity owner;

  public ArtistEntity() {
    this(Defaults.DEFAULT_STR, Lists.newArrayList(), new UserEntity());
  }

  public ArtistEntity(String name, UserEntity owner) {
    this(Defaults.getDefaultLong(), name, Lists.newArrayList(), owner);
  }

  public ArtistEntity(String name, List<AlbumEntity> album, UserEntity owner) {
    this(Defaults.getDefaultLong(), name, album, owner);
  }

  public ArtistEntity(Long id, String name, List<AlbumEntity> album, UserEntity owner) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.albums = MoreObjects.firstNonNull(album, Lists.newArrayList());
    this.setOwner(owner);
  }

  // Getters
  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ImmutableList<AlbumEntity> getAlbums() {
    return ImmutableList.copyOf(albums);
  }

  public UserEntity getOwner() {
    return owner;
  }


  // Setters
  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAlbums(List<AlbumEntity> albumList) {
    this.albums = MoreObjects.firstNonNull(albumList, Lists.newArrayList());
  }


  // Table relationship handlers
  public void addAlbum(AlbumEntity albumEntity) {
    this.addAlbum(albumEntity, true);
  }

  public void addAlbums(List<AlbumEntity> albumEntities) {
    albumEntities.forEach(entity -> this.addAlbum(entity, true));
  }

  void addAlbum(AlbumEntity albumEntity, boolean shouldAdd) {
    if (albumEntity == null) return;

    if (this.albums.contains(albumEntity))
      this.albums.set(this.getAlbums().indexOf(albumEntity), albumEntity);
    else
      this.albums.add(albumEntity);

    if (shouldAdd)
      albumEntity.setArtist(this, false);
  }

  public void removeAlbum(AlbumEntity albumEntity) {
    this.albums.remove(albumEntity);
    albumEntity.setArtist(null);
  }

  public void removeAlbums(List<AlbumEntity> albumEntities) {
    albumEntities.forEach(this::removeAlbum);
  }

  public void setOwner(UserEntity owner) {
    this.setOwner(owner, true);
  }

  void setOwner(UserEntity owner, boolean shouldAdd) {
    this.owner = owner;

    if (owner != null && shouldAdd) {
      owner.addArtist(this, false);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArtistEntity that = (ArtistEntity) o;
    return id == that.id
        && name.equals(that.name)
        && owner.equals(that.owner);
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

    node.put("created_by", this.owner.getName());

    return node;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("name", this.name)
        .add("albums", this.albums)
        .add("created_by", this.owner)
        .toString();
  }
}
