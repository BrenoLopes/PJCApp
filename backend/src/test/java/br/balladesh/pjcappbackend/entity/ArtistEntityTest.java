package br.balladesh.pjcappbackend.entity;

import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtistEntityTest {

  @Test
  void getId() {
    long expected = 1;
    ArtistEntity target = new ArtistEntity(expected,"AmongUs", Lists.newArrayList());

    assertEquals(expected, target.getId());
  }

  @Test
  void getName() {
    String expected = "AmongUs";
    ArtistEntity target = new ArtistEntity(expected, Lists.newArrayList());

    assertEquals(expected, target.getName());
  }

  @Test
  void setName() {
    String expected = "AmongUs";
    ArtistEntity target = new ArtistEntity(null, Lists.newArrayList());
    target.setName(expected);

    assertEquals(expected, target.getName());
  }

  @Test
  void getAlbums() {
    ArtistEntity target = new ArtistEntity("AmongUs", Lists.newArrayList());

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