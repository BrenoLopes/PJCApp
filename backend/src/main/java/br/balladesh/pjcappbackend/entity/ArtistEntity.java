package br.balladesh.pjcappbackend.entity;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

  @OneToMany(mappedBy = "artist")
  private final List<AlbumEntity> albums;

  public ArtistEntity() {
    this.name = Defaults.DEFAULT_STR;
    this.albums = Lists.newArrayList();
  }

  public ArtistEntity(String name, List<AlbumEntity> album) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.albums = MoreObjects.firstNonNull(album, Lists.newArrayList());
  }

  public ArtistEntity(Long id, String name, List<AlbumEntity> album) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.albums = MoreObjects.firstNonNull(album, Lists.newArrayList());
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArtistEntity that = (ArtistEntity) o;
    return id == that.id
        && Objects.equal(this.name, that.name)
        && Objects.equal(this.albums, that.albums);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, albums);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("name", this.name)
        .add("albums", this.albums)
        .toString();
  }
}
