package br.balladesh.pjcappbackend.controllers.security.refresh;

import br.balladesh.pjcappbackend.config.security.jwt.JwtStatus;
import br.balladesh.pjcappbackend.config.security.jwt.JwtUtilities;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.security.JwtJsonResponse;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.services.UsersService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@TestPropertySource("/application.test.properties")
class RefreshControllerTest {

  @Mock
  UsersService usersService;

  @Mock
  HttpServletRequest request;

  @Autowired
  JwtUtilities jwtUtilities;

  @Test
  void testTokenIsStillValid() {
    String username = "ohno@ohno.com";
    String token = this.jwtUtilities.generateJwtToken(username);

    Mockito.when(this.request.getAttribute("jwt_is_valid")).thenReturn(true);
    Mockito.when(this.request.getAttribute("jwt_token")).thenReturn(token);
    Mockito.when(this.request.getAttribute("username")).thenReturn(username);

    RefreshController testTarget = new RefreshController(this.jwtUtilities, usersService);

    ResponseEntity<JwtJsonResponse> expected = new ResponseEntity<>(
        new JwtJsonResponse(username, token),
        HttpStatus.OK
    );

    ResponseEntity<?> received = testTarget.refreshJwtToken(this.request);

    assertEquals(expected.getStatusCode(), received.getStatusCode());
    assertEquals(expected.getBody(), received.getBody());
  }

  @Test
  void testTokenNotValid() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    this.setJwtTokenExpiration(10L);
    String username = "ohno@ohno.com";
    String token = this.jwtUtilities.generateJwtToken(username);

    assertSame(JwtStatus.EXPIRED, this.jwtUtilities.checkJwtStatus(token));

    ExpiredJwtException exception = this.jwtUtilities.getExceptionFromExpiredToken(token)
        .orElseThrow(NullPointerException::new);

    Mockito.when(this.request.getAttribute("claims")).thenReturn(exception.getClaims());
    Mockito.when(this.usersService.getUserBy(anyString()))
        .thenReturn(Optional.of(new UserEntity("ohno","ohno@ohno.com", "ohno")));

    this.setJwtTokenExpiration(10000);

    RefreshController testTarget = new RefreshController(this.jwtUtilities, usersService);
    ResponseEntity<?> received = testTarget.refreshJwtToken(this.request);

    if (!(received.getBody() instanceof JwtJsonResponse)) {
      fail();
    }

    JwtJsonResponse body = (JwtJsonResponse) received.getBody();

    assertSame(HttpStatus.OK, received.getStatusCode());
    assertEquals(username, body.getUsername());
    assertNotEquals(token, body.getToken());
  }

  @Test
  void testInternalServerError() {
    RefreshController testTarget = new RefreshController(this.jwtUtilities, usersService);
    ResponseEntity<?> received = testTarget.refreshJwtToken(this.request);

    if (!(received.getBody() instanceof MessageResponse))
      fail();

    MessageResponse m = (MessageResponse) received.getBody();

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, received.getStatusCode());
    assertEquals("The request couldn't be completed because an error happened in the server.", m.getMessage());
  }

  private void setJwtTokenExpiration(long milliseconds) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method reflection = JwtUtilities.class.getDeclaredMethod("setExpiration", Long.class);
    reflection.setAccessible(true);
    reflection.invoke(this.jwtUtilities, milliseconds);
  }
}
