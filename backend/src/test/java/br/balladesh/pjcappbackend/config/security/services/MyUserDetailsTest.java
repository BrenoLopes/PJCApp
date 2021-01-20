package br.balladesh.pjcappbackend.config.security.services;

import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyUserDetailsTest {

  @Test
  void getFromValidData() {
    long id = 1;
    String username = "ohno@ohno.com";
    String email = "ohno@ohno.com";
    String password = "ohno123456";
    MyUserDetails testTarget = new MyUserDetails(id, username, email, password, Lists.newArrayList());

    assertEquals(id, testTarget.getId());
    assertEquals(username, testTarget.getUsername());
    assertEquals(email, testTarget.getEmail());
    assertEquals(password, testTarget.getPassword());
  }

  @Test
  void getDefaultFromInvalidInput() {
    MyUserDetails testTarget = new MyUserDetails(null, null, null, null, null);

    assertEquals(Defaults.getDefaultLong(), testTarget.getId());
    assertEquals(Defaults.DEFAULT_STR, testTarget.getUsername());
    assertEquals(Defaults.DEFAULT_STR, testTarget.getEmail());
    assertEquals(Defaults.DEFAULT_STR, testTarget.getPassword());
  }
}