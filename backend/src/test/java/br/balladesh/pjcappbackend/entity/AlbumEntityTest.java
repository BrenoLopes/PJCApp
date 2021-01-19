package br.balladesh.pjcappbackend.entity;

import br.balladesh.pjcappbackend.utilities.Defaults;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlbumEntityTest {

  @Test
  void getId() {
    long expected = 1;
    AlbumEntity target = new AlbumEntity(
        expected,
        "ohno",
        new ArtistEntity("amongus", Lists.newArrayList()),
        Defaults.DEFAULT_STR
    );

    assertEquals(expected, target.getId());
  }

  @Test
  void setId() {
    long expected = 12;
    AlbumEntity target = new AlbumEntity(null, null, null, null);
    target.setId(expected);

    assertEquals(expected, target.getId());
  }

  @Test
  void getName() {
    String expected = "ohno";
    AlbumEntity target = new AlbumEntity(
        1L,
        expected,
        new ArtistEntity("amongus", Lists.newArrayList()),
        Defaults.DEFAULT_STR
    );

    assertEquals(expected, target.getName());
  }

  @Test
  void setName() {
    String expected = "ohno";
    AlbumEntity target = new AlbumEntity(null, null, null, null);
    target.setName(expected);

    assertEquals(expected, target.getName());
  }

  @Test
  void getImage() {
    String expected = "FakeMinIOObjectName";
    AlbumEntity target = new AlbumEntity(
        1L,
        "ohno",
        new ArtistEntity("amongus", Lists.newArrayList()),
        expected
    );

    assertEquals(expected, target.getImage());
  }

  @Test
  void setImage() {
    String expected = "FakeMinIOObjectName";
    AlbumEntity target = new AlbumEntity(null, null, null, null);
    target.setImage(expected);

    assertEquals(expected, target.getImage());
  }

  @Test
  void getArtist() {
    ArtistEntity expected = new ArtistEntity("AmongUs", Lists.newArrayList());

    AlbumEntity target = new AlbumEntity(
        1L,
        "ohno",
        expected,
        Defaults.DEFAULT_STR
    );

    assertEquals(expected, target.getArtist());
  }

  @Test
  void setArtist() {
    ArtistEntity expected = new ArtistEntity("AmongUs", Lists.newArrayList());
    AlbumEntity target = new AlbumEntity(null, null, null, null);
    target.setArtist(expected);

    assertEquals(expected, target.getArtist());
  }

  @Test
  void toJson() {
    ArtistEntity artist = new ArtistEntity("AmongUs", Lists.newArrayList());
    AlbumEntity target = new AlbumEntity(1L, "ohno", artist, "FakeMinIOObjectName");

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode expected = mapper.createObjectNode();
    expected.put("id", 1);
    expected.put("name", "ohno");
    expected.put("image", "FakeMinIOObjectName");
    expected.put("artistName", "AmongUs");

    ObjectNode received = target.toJson();

    assertEquals(expected.toString(), received.toString());
  }

  @Test
  void getFromNull() {
    AlbumEntity target = new AlbumEntity(null, null, null, null);

    assertEquals(Defaults.DEFAULT_STR, target.getName());
    assertEquals(Defaults.DEFAULT_STR, target.getImage());
    assertEquals(Defaults.getDefaultLong(), target.getId());
    assertEquals(new ArtistEntity(), target.getArtist());
  }
}