package br.balladesh.pjcappbackend.controllers.security.signup;

import br.balladesh.pjcappbackend.utilities.Result;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckEmailCommandTest {
  @Test
  void testValidEmail() {
    String email1 = "ohno@ohno.com";
    String email2 = "amongus@hello.to";

    CheckEmailCommand command1 = new CheckEmailCommand(email1);
    CheckEmailCommand command2 = new CheckEmailCommand(email2);

    Result<Boolean, HttpException> result1 = command1.execute();
    Result<Boolean, HttpException> result2 = command2.execute();

    assertTrue(result1.haveData());
    assertTrue(result2.haveData());

    assertEquals(true, result1.getData());
    assertEquals(true, result2.getData());
    assertNull(result1.getException());
    assertNull(result2.getException());
  }

  void testInValidEmail() {
    String email1 = "ohno@.com";
    String email2 = "amongushello.to";
    String email3 = "";

    CheckEmailCommand command1 = new CheckEmailCommand(email1);
    CheckEmailCommand command2 = new CheckEmailCommand(email2);
    CheckEmailCommand command3 = new CheckEmailCommand(email3);

    Result<Boolean, HttpException> result1 = command1.execute();
    Result<Boolean, HttpException> result2 = command2.execute();
    Result<Boolean, HttpException> result3 = command3.execute();

    assertTrue(result1.haveData());
    assertTrue(result2.haveData());
    assertTrue(result3.haveData());

    assertEquals(true, result1.getData());
    assertEquals(true, result2.getData());
    assertEquals(true, result3.getData());
    assertNull(result1.getException());
    assertNull(result2.getException());
    assertNull(result3.getException());
  }
}