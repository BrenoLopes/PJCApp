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

  @Value("${jwt.expiration}")
  long expiration;

  @Test
  void testJwtTokenGeneration() {
    MyUserDetails userCredentials = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        Lists.newArrayList()
    );

    Mockito.when(this.authentication.getPrincipal()).thenReturn(userCredentials);
    String token = this.jwtUtilities.generateJwtToken(this.authentication);

    assertNotNull(token);
    assertNotEquals(token, "");
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
  void testRefreshJwtToken() throws InterruptedException {
    MyUserDetails userCredentials = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        Lists.newArrayList()
    );
    Mockito.when(this.authentication.getPrincipal()).thenReturn(userCredentials);

    String beforeToken = this.jwtUtilities.generateJwtToken(this.authentication);

    Thread.sleep(this.expiration + 400);
    assertSame(JwtStatus.EXPIRED, this.jwtUtilities.checkJwtStatus(beforeToken));

    String refreshToken = this.jwtUtilities.generateRefreshToken("ohno@ohno.com");
    assertSame(JwtStatus.VALID, this.jwtUtilities.checkJwtStatus(refreshToken));
  }

  @Test
  void testExtractTokenFromHeader() {
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2MTExNzc2MDAsImV4cCI6MTYxMTE3NzYwMCwic3ViIjoib2hub0BvaG5vLmNvbSJ9.-Ikj7BiZ_SpPPhEfrRHEulPFNBy69UU120f5bUhJfPMpYr9swkbE-FspZ6D0ij7Lrt8DQe6FMkH-b0pDsuGp2Q";
    String header = "Bearer " + token;
    String received = this.jwtUtilities.extractTokenFromHeader(header).orElse("");

    assertEquals(token, received);
  }

  @Test
  void testValidJwtToken() throws InterruptedException {
    MyUserDetails userCredentials = new MyUserDetails(
        1L,
        "ohno@ohno.com",
        "ohno@ohno.com",
        "123456",
        Lists.newArrayList()
    );

    Mockito.when(this.authentication.getPrincipal()).thenReturn(userCredentials);

    String token = this.jwtUtilities.generateJwtToken(this.authentication);
    assertSame(JwtStatus.VALID, this.jwtUtilities.checkJwtStatus(token));

    Thread.sleep(this.expiration + 100);
    assertSame(JwtStatus.EXPIRED, this.jwtUtilities.checkJwtStatus(token));

    token = "InvalidToken";
    assertSame(JwtStatus.CORRUPTED, this.jwtUtilities.checkJwtStatus(token));

    token = "";
    assertSame(JwtStatus.CORRUPTED, this.jwtUtilities.checkJwtStatus(token));
  }
}
