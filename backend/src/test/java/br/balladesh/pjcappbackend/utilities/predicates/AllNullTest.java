package br.balladesh.pjcappbackend.utilities.predicates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllNullTest {
  @Test
  public void singleNull() {
    final String one = null, two = "123", three = "no";

    assertFalse(AllNull.withParams(one, two, three).check());
  }

  @Test
  public void everythingNull() {
    final String one = null, two = null, three = null;

    assertTrue(AllNull.withParams(one, two, three).check());
  }
}