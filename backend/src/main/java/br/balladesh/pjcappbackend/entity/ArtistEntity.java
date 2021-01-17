package br.balladesh.pjcappbackend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity(name = "artists")
public class ArtistEntity {
  public ArtistEntity() {
    this.name = "";
    this.albums = new ArrayList<>();
  }

  public ArtistEntity(String name, List<AlbumEntity> album) {
    this.name = name == null ? "" : name;
    this.albums = album == null ? new ArrayList<>() : album;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(mappedBy = "artist")
  private List<AlbumEntity> albums;

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<AlbumEntity> getAlbums() {
      return albums;
  }

  @Override
  public String toString() {
    String str
        = "{"
        + "id=%d,"
        + "name=\"%s\","
        + "albums=[%s]"
        + "}";

    StringBuilder albumsStr = new StringBuilder();
    for(AlbumEntity a : this.albums) {
      albumsStr.append(a.toString());
      albumsStr.append(',');
    }
    albumsStr.deleteCharAt(albumsStr.length() - 1);

    return String.format(
        str,
        this.id,
        this.name,
        albumsStr.toString()
    );
  }
}
