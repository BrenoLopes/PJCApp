package br.balladesh.pjcappbackend.entity;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.persistence.*;
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
  @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "owner")
  private List<ArtistEntity> artists;

  public UserEntity() {
    this(Defaults.getDefaultLong(), Defaults.DEFAULT_STR, Defaults.DEFAULT_STR, Defaults.DEFAULT_STR, Lists.newArrayList());
  }

  public UserEntity(String name, String email, String password) {
    this(Defaults.getDefaultLong(), name, email, password, Lists.newArrayList());
  }

  public UserEntity(String name, String email, String password, List<ArtistEntity> artists) {
    this(Defaults.getDefaultLong(), name, email, password, artists);
  }

  public UserEntity(long id, String name, String email, String password, List<ArtistEntity> artists) {
    this.id = id;
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
    this.email = MoreObjects.firstNonNull(email, Defaults.DEFAULT_STR);
    this.password = MoreObjects.firstNonNull(password, Defaults.DEFAULT_STR);
    this.artists = MoreObjects.firstNonNull(artists, Lists.newArrayList());
  }


  // Getters
  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public ImmutableList<ArtistEntity> getArtists() {
    return ImmutableList.copyOf(this.artists);
  }


  // Setters
  public void setName(String name) {
    this.name = MoreObjects.firstNonNull(name, Defaults.DEFAULT_STR);
  }

  public void setEmail(String email) {
    this.email = MoreObjects.firstNonNull(email, Defaults.DEFAULT_STR);
  }

  public void setPassword(String password) {
    this.password = MoreObjects.firstNonNull(password, Defaults.DEFAULT_STR);
  }

  public void setArtists(List<ArtistEntity> artistList) {
    this.artists = MoreObjects.firstNonNull(artistList, Lists.newArrayList());
  }

  public void addArtists(List<ArtistEntity> artistsList) {
    artistsList.forEach(artist -> this.addArtist(artist, true));
  }

  public void addArtist(ArtistEntity artistEntity) {
    this.addArtist(artistEntity, true);
  }

  void addArtist(ArtistEntity artistEntity, boolean shouldAdd) {
    if (artistEntity == null)
      return;

    if (this.artists.contains(artistEntity)) {
      this.artists.set(this.artists.indexOf(artistEntity), artistEntity);
    } else {
      this.artists.add(artistEntity);
    }

    if (shouldAdd)
      artistEntity.setOwner(this, false);
  }

  public void removeArtists(List<ArtistEntity> artistsList) {
    artistsList.forEach(this::removeArtist);
  }

  public void removeArtist(ArtistEntity artistEntity) {
    this.artists.remove(artistEntity);
    artistEntity.setOwner(null);
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
