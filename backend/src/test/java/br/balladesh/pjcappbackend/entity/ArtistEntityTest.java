package br.balladesh.pjcappbackend.entity;

import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtistEntityTest {

  private UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");

  @Test
  void getId() {
    long expected = 1;
    ArtistEntity target = new ArtistEntity(expected,"AmongUs", Lists.newArrayList(), robotUser);

    assertEquals(expected, target.getId());
  }

  @Test
  void getName() {
    String expected = "AmongUs";
    ArtistEntity target = new ArtistEntity(expected, Lists.newArrayList(), robotUser);

    assertEquals(expected, target.getName());
  }

  @Test
  void setName() {
    String expected = "AmongUs";
    ArtistEntity target = new ArtistEntity(null, Lists.newArrayList(), robotUser);
    target.setName(expected);

    assertEquals(expected, target.getName());
  }

  @Test
  void getAlbums() {
    ArtistEntity target = new ArtistEntity("AmongUs", Lists.newArrayList(), robotUser);

    AlbumEntity albumOne = new AlbumEntity("ohno1", target, "FakeMinIOObject");
    AlbumEntity albumTwo = new AlbumEntity("ohno2", target, "FakeMinIOObject");
    AlbumEntity albumThree = new AlbumEntity("ohno3", target, "FakeMinIOObject");

    target.getAlbums().add(albumOne);
    target.getAlbums().add(albumTwo);
    target.getAlbums().add(albumThree);

    List<AlbumEntity> expected = ImmutableList.copyOf(Lists.newArrayList(albumOne, albumTwo, albumThree));

    assertEquals(expected, target.getAlbums());
  }
}