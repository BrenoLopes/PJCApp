package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagedAlbumResponseBodyTest {

  PagedAlbumResponseBody target;
  AlbumEntity album = new AlbumEntity("randomname", "");

  public PagedAlbumResponseBodyTest() {
    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
    ArtistEntity artist = new ArtistEntity("randomartist", robotUser);
    artist.addAlbum(album);

    this.target = new PagedAlbumResponseBody(
        ImmutableList.of(album),
        0,
        1,
        1
    );
  }

  @Test
  void getAlbums() {
    ImmutableList<AlbumEntity> expected = ImmutableList.of(this.album);
    ImmutableList<AlbumEntity> received = this.target.getAlbums();

    assertEquals(expected, received);
  }

  @Test
  void getCurrentPage() {
    int expected = 0;
    int received = this.target.getCurrentPage();

    assertEquals(expected, received);
  }

  @Test
  void getTotalPages() {
    int expected = 1;
    int received = this.target.getTotalPages();

    assertEquals(expected, received);
  }

  @Test
  void getTotalItems() {
    long expected = 1;
    long received = this.target.getTotalItems();

    assertEquals(expected, received);
  }

  @Test
  void getFromNullValues() {
    ImmutableList<AlbumEntity> expected = ImmutableList.of();
    PagedAlbumResponseBody target = new PagedAlbumResponseBody(null, 0, 0, 0);
    ImmutableList<AlbumEntity> received = target.getAlbums();

    assertTrue(expected.equals(received));
  }
}