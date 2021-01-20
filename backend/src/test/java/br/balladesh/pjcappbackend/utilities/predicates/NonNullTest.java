package br.balladesh.pjcappbackend.utilities.predicates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonNullTest {

  @Test
  public void withoutNull() {
    String var1 = "1";
    String var2 = "2";

    assertFalse(NonNull.withParams(var1, var2).check());
  }

  @Test
  public void withNull() {
    String var1 = "1";
    String var2 = null;

    assertTrue(NonNull.withParams(var1, var2).check());
  }

}