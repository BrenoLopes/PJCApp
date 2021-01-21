package br.balladesh.pjcappbackend.config.security.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtStatusTest {
  JwtStatus status1 = JwtStatus.VALID;
  JwtStatus status2 = JwtStatus.CORRUPTED;
  JwtStatus status3 = JwtStatus.EXPIRED;

  @Test
  void hasFields() {
    assertEquals(JwtStatus.VALID, status1);
    assertEquals(JwtStatus.CORRUPTED, status2);
    assertEquals(JwtStatus.EXPIRED, status3);
  }
}