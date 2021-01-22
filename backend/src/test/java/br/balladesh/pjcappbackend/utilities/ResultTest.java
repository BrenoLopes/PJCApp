package br.balladesh.pjcappbackend.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

  @Test
  void fromError() {
    Exception expected = new Exception("Testando");
    Result<Void, Exception> result = Result.fromError(expected);

    assertEquals(expected, result.getException());
  }

  @Test
  void fromErrorWithAMessage() {
    Exception expected = new Exception("Testando");
    Result<Void, Exception> result = Result.fromError(expected.getMessage());

    assertEquals(expected.getMessage(), result.getException().getMessage());
  }

  @Test
  void from() {
    boolean expected = true;
    Result<Boolean, Exception> result = Result.from(expected);

    assertTrue(result.haveData());
    assertEquals(expected, result.getData());
  }

  @Test
  void haveData() {
    boolean expected = true;
    Result<Boolean, Exception> result = Result.from(expected);

    assertTrue(result.haveData());
  }
}