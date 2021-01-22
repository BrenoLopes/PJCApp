package br.balladesh.pjcappbackend.utilities.predicates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HasNullTest {

  @Test
  public void withoutNull() {
    String var1 = "1";
    String var2 = "2";

    assertFalse(HasNull.withParams(var1, var2).check());
  }

  @Test
  public void withNull() {
    String var1 = "1";
    String var2 = null;

    assertTrue(HasNull.withParams(var1, var2).check());
  }

}