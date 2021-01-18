package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.utilities.Defaults;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedAlbumResponseBodyTest {

  PagedAlbumResponseBody target;
  AlbumEntity album;

  public PagedAlbumResponseBodyTest() {
    ArtistEntity artist = new ArtistEntity("randomartist", Lists.newArrayList());
    this.album = new AlbumEntity("randomname", artist, Defaults.DEFAULT_STR);

    artist.getAlbums().add(album);

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