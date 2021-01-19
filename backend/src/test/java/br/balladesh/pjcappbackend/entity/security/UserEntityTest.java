package br.balladesh.pjcappbackend.entity.security;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

  @Test
  void getId() {
    UserEntity userEntity = new UserEntity("ohno", "ohno@ohno.com", "123456");

    assertEquals(Defaults.getDefaultLong(), userEntity.getId());
  }

  @Test
  void getName() {
    String expected = "ohno";
    UserEntity userEntity = new UserEntity(expected, "ohno@ohno.com", "123456");

    assertEquals(expected, userEntity.getName());
  }

  @Test
  void setName() {
    String expected = "ohno";
    UserEntity userEntity = new UserEntity(null, "ohno@ohno.com", "123456");
    userEntity.setName(expected);

    assertEquals(expected, userEntity.getName());
  }

  @Test
  void getEmail() {
    String expected = "ohno@ohno.com";
    UserEntity userEntity = new UserEntity("ohno", expected, "123456");

    assertEquals(expected, userEntity.getEmail());
  }

  @Test
  void setEmail() {
    String expected = "ohno@ohno.com";
    UserEntity userEntity = new UserEntity(null, null, "123456");
    userEntity.setEmail(expected);

    assertEquals(expected, userEntity.getEmail());
  }

  @Test
  void getPassword() {
    String expected = "123456";
    UserEntity userEntity = new UserEntity("ohno", "ohno@ohno.com", expected);

    assertEquals(expected, userEntity.getPassword());
  }

  @Test
  void setPassword() {
    String expected = "123456";
    UserEntity userEntity = new UserEntity("ohno", "ohno@ohno.com", null);
    userEntity.setPassword(expected);

    assertEquals(expected, userEntity.getPassword());
  }
}