package br.balladesh.pjcappbackend.entity.security;

import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @JsonIgnore
  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "createdByUser")
  private final List<ArtistEntity> artists;

  public UserEntity() {
    this.id = Defaults.getDefaultLong();
    this.name = Defaults.DEFAULT_STR;
    this.email = Defaults.DEFAULT_STR;
    this.password = Defaults.DEFAULT_STR;
    this.artists = new ArrayList<>();
  }

  public UserEntity(String name, String email, String password) {
    this.id = Defaults.getDefaultLong();
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.email = MoreObjects.firstNonNull(email, Defaults.DEFAULT_STR);
    this.password = MoreObjects.firstNonNull(password, Defaults.DEFAULT_STR);
    this.artists = new ArrayList<>();
  }

  public UserEntity(String name, String email, String password, List<ArtistEntity> artists) {
    this.id = Defaults.getDefaultLong();
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.email = MoreObjects.firstNonNull(email, Defaults.DEFAULT_STR);
    this.password = MoreObjects.firstNonNull(password, Defaults.DEFAULT_STR);
    this.artists = MoreObjects.firstNonNull(artists, new ArrayList<>());
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = MoreObjects.firstNonNull(email, Defaults.DEFAULT_STR);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = MoreObjects.firstNonNull(password, Defaults.DEFAULT_STR);
  }

  public List<ArtistEntity> getArtists() {
    return artists;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserEntity that = (UserEntity) o;
    return id == that.id
        && Objects.equal(this.name, that.name)
        && Objects.equal(this.email, that.email)
        && Objects.equal(this.password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, email, password);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("name", this.name)
        .add("email", this.email)
        .add("password", this.password)
        .toString();
  }
}
