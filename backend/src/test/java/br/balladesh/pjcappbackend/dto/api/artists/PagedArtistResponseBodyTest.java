package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedArtistResponseBodyTest {

  private final PagedArtistResponseBody testTarget;
  private final List<ArtistEntity> artists;

  private PagedArtistResponseBodyTest() {
    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
    this.artists = Lists.newArrayList(new ArtistEntity("AnArtist", Lists.newArrayList(), robotUser));

    this.testTarget = new PagedArtistResponseBody(this.artists, 0, 1, 1);
  }

  @Test
  void getArtists() {
    assertEquals(this.artists, this.testTarget.getArtists());
  }

  @Test
  void getCurrentPage() {
    int expected = 0;

    assertEquals(expected, this.testTarget.getCurrentPage());
  }

  @Test
  void getTotalPages() {
    int expected = 1;
    assertEquals(expected, this.testTarget.getTotalPages());
  }

  @Test
  void getTotalItems() {
    long expected = 1;
    assertEquals(expected, this.testTarget.getTotalItems());
  }

  @Test
  void getFromNull() {
    PagedArtistResponseBody toTest = new PagedArtistResponseBody(null, 0, 0, 0);
    assertEquals(ImmutableList.of(), toTest.getArtists());
    assertEquals(0, toTest.getCurrentPage());
    assertEquals(0, toTest.getTotalItems());
    assertEquals(0, toTest.getTotalPages());
  }
}