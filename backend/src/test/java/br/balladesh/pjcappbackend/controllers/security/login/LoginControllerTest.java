package br.balladesh.pjcappbackend.controllers.security.login;

import br.balladesh.pjcappbackend.config.security.jwt.JwtUtilities;
import br.balladesh.pjcappbackend.config.security.services.MyUserDetails;
import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.security.JwtJsonResponse;
import br.balladesh.pjcappbackend.dto.security.UserLoginRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
  @Mock
  AuthenticationManager authenticationManager;

  @Autowired
  JwtUtilities jwtUtilities;

  @Test
  void checkValidUser() {
    MyUserDetails details = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        null
    );

    // First setup a username in context
    Authentication inputAuth = new UsernamePasswordAuthenticationToken(
        details.getUsername(),
        details.getPassword()
    );

    Authentication outputAuth = new UsernamePasswordAuthenticationToken(
        details,
        null,
        Lists.newArrayList()
    );

    Mockito.when(this.authenticationManager.authenticate(inputAuth)).thenReturn(outputAuth);

    ResponseEntity<?> received = new LoginController(this.authenticationManager,this.jwtUtilities)
        .authUser(new UserLoginRequest("ohno@ohno.com", "123456"));

    if (received.getStatusCode() != HttpStatus.OK)
      fail("A login message should have been returned");

    JwtJsonResponse body = (JwtJsonResponse) received.getBody();

    if (body == null)
      fail("A login message should have been returned");

    assertEquals(details.getUsername(), body.getUsername());
    assertNotEquals(body.getToken(), "");
    assertNotNull(body.getToken());
  }

  @Test
  void checkInvalidUser() {
    MyUserDetails details = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        null
    );

    ResponseEntity<?> received = new LoginController(this.authenticationManager,this.jwtUtilities)
        .authUser(new UserLoginRequest("ohno@ohno.com", "123456"));

    assertSame(received.getStatusCode(), HttpStatus.UNAUTHORIZED);
    assertEquals(new MessageResponse("Your credentials is incorrect!"), received.getBody());
  }
}