package br.balladesh.pjcappbackend.config.security.jwt;

import br.balladesh.pjcappbackend.config.security.services.MyUserDetails;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource("/application.test.properties")
class JwtUtilitiesTest {
  @Autowired
  JwtUtilities jwtUtilities;

  @Mock
  Authentication authentication;

  @Test
  void testGenerateJwtTokenWithAuthentication() {
    MyUserDetails userCredentials = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        Lists.newArrayList()
    );

    Mockito.when(this.authentication.getPrincipal()).thenReturn(userCredentials);
    String token = this.jwtUtilities.generateJwtToken(this.authentication);

    if (token == null || token.equals(""))
      fail();

    assertSame(this.jwtUtilities.checkJwtStatus(token), JwtStatus.VALID);
  }

  @Test
  void testGenerateRefreshJwtToken() {
    // Lower the expiration time
    this.jwtUtilities.setExpiration(10);

    // Generate first token
    String firstToken = this.jwtUtilities.generateJwtToken("ohno@ohno.com");
    JwtStatus status = this.jwtUtilities.checkJwtStatus(firstToken);

    assertSame(JwtStatus.EXPIRED, status);

    this.jwtUtilities.setExpiration(6000);

    // Generate second token
    String secondToken = this.jwtUtilities.generateJwtToken("ohno@ohno.com");
    status = this.jwtUtilities.checkJwtStatus(secondToken);

    assertSame(JwtStatus.VALID, status);
    assertNotEquals(firstToken, secondToken);
  }

  @Test
  void extractUsername() {
    String expected = "ohno@ohno.com";

    MyUserDetails userCredentials = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        Lists.newArrayList()
    );
    Mockito.when(this.authentication.getPrincipal()).thenReturn(userCredentials);

    String token = this.jwtUtilities.generateJwtToken(this.authentication);
    String received = this.jwtUtilities.extractUsername(token).orElse("");

    assertEquals(expected, received);
  }

  @Test
  void testExtractTokenFromHeader() {
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2MTExNzc2MDAsImV4cCI6MTYxMTE3NzYwMCwic3ViIjoib2hub0BvaG5vLmNvbSJ9.-Ikj7BiZ_SpPPhEfrRHEulPFNBy69UU120f5bUhJfPMpYr9swkbE-FspZ6D0ij7Lrt8DQe6FMkH-b0pDsuGp2Q";
    String header = "Bearer " + token;
    String received = this.jwtUtilities.extractTokenFromHeader(header).orElse("");

    assertEquals(token, received);
  }

  @Test
  void testValidJwtToken() {
    MyUserDetails userCredentials = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        Lists.newArrayList()
    );
    Mockito.when(this.authentication.getPrincipal()).thenReturn(userCredentials);

    String token = "InvalidToken";
    assertSame(JwtStatus.CORRUPTED, this.jwtUtilities.checkJwtStatus(token));

    token = "";
    assertSame(JwtStatus.CORRUPTED, this.jwtUtilities.checkJwtStatus(token));

    this.jwtUtilities.setExpiration(50);
    token = this.jwtUtilities.generateJwtToken(this.authentication);
    assertSame(JwtStatus.EXPIRED, this.jwtUtilities.checkJwtStatus(token));

    this.jwtUtilities.setExpiration(5000);
    token = this.jwtUtilities.generateJwtToken(this.authentication);
    assertSame(JwtStatus.VALID, this.jwtUtilities.checkJwtStatus(token));
  }
}
