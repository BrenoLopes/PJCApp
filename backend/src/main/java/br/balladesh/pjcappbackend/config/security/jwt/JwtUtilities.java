package br.balladesh.pjcappbackend.config.security.jwt;

import br.balladesh.pjcappbackend.config.security.services.MyUserDetails;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.util.*;

public class JwtUtilities {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtilities.class);

  @Value("${jwt.secret-key}")
  private String jwtSecret = Defaults.DEFAULT_STR;

  @Value("${jwt.expiration}")
  private long expiration = Defaults.getDefaultLong();

  /**
   * Generate a jwt token using the userdetails object inside the authentication object
   *
   * @param authentication Spring Boot Authentication Object
   * @return Jwt string
   */
  public String generateJwtToken(Authentication authentication) {
    MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
    return this.buildJwtToken(userDetails.getUsername());
  }

//  /**
//   * Generate a jwt token using an expired jwt string while using the claims stored in the
//   * expired jwt string
//   *
//   * @param claims Claims stored inside an expired jwt token
//   * @param username The user's email address that owns the token
//   * @return Jwt string
//   */
//  public String generateRefreshToken(Map<String, Object> claims, String username) {
//    return this.buildJwtToken(claims, username);
//  }

  /**
   * Generate a jwt token using an expired jwt string by using only the user's email address
   *
   * @param username The user's email address that owns the token
   * @return Jwt string
   */
  public String generateJwtToken(String username) {
    return this.buildJwtToken(username);
  }

  /**
   * Retrieves the username stored inside a jwt token
   *
   * @param token A valid jwt token signed with the app's secret key
   * @return The user's email address who own's the token or <code>Options.empty()</code> if the key is invalid
   * or corrupted.
   */
  public Optional<String> extractUsername(String token) {
    Optional<String> username;

    try {
      final String subject = Jwts.parser()
          .setSigningKey(this.jwtSecret)
          .parseClaimsJws(token)
          .getBody()
          .getSubject();

      username = Optional.of(subject);
    } catch (ExpiredJwtException e) {
      username = Optional.of(e.getClaims().getSubject());
    } catch(Exception e) {
      username = Optional.empty();
    }

    return username;
  }

  /**
   * Verifies if the jwt token was signed using this secret secret key, is in right format
   * and was not yet expired.
   *
   * @param token Jwt token to be checked
   * @return <code>true</code> if is valid and not expired or <code>false</code> otherwise
   */
  public JwtStatus checkJwtStatus(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return JwtStatus.VALID;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
      return JwtStatus.EXPIRED;
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return JwtStatus.CORRUPTED;
  }

  /**
   * Case the author chose to put the claims and header inside an exception, I have
   * to get this exception to refresh the token later
   *
   * @param token An expired token. It'll return nothing if is not in expired state.
   * @return ExpiredJwtException
   */
  public Optional<ExpiredJwtException> getExceptionFromExpiredToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    } catch(ExpiredJwtException e) {
      return Optional.of(e);
    }
    return Optional.empty();
  }

  /**
   * Removes the jwt token from the authentication header, striping the Bearer prefix
   *
   * @param authorizationHeader Authorization header with format Bearer token
   * @return the jwt token wrapped in an optional if successful or empty if it was not formatted right.
   */
  public Optional<String> extractTokenFromHeader(String authorizationHeader) {
    if( StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
      return Optional.of(authorizationHeader.substring(7));
    }
    return Optional.empty();
  }

  protected void setExpiration(Long milliseconds) {
    this.expiration = milliseconds;
  }

  /**
   * Create a jwt token using an email address
   *
   * @param username The user's email address who owns the token
   * @return jwt token
   */
  private String buildJwtToken(String username) {
    return Jwts.builder()
        .setIssuedAt(new Date())
        .setExpiration(
            new Date(System.currentTimeMillis() + this.expiration)
        )
        .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
        .setSubject(String.valueOf(username))
        .compact();
  }

  /**
   * <p>Create a jwt token by using the user's email address with a set of claims.</p>
   * <p>Be careful while using this, because if you copy the claims of an expired jwt token,
   * it will still be in an expired state.</p>
   *
   * @param claims Jwt token claims, like expiration etc.
   * @param username The user's email address
   * @return jwt token
   */
  private String buildJwtToken(Map<String, Object> claims, String username) {
    return Jwts.builder()
        .setIssuedAt(new Date())
        .setExpiration(
            new Date(System.currentTimeMillis() + this.expiration)
        )
        .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
        .setClaims(claims)
        .setSubject(String.valueOf(username))
        .compact();
  }

//  /**
//   * A builder to build the jwt builder to build the token and make the code DRY. LOL
//   *
//   * @return The JwtBuilder for method chaining.
//   */
//  private JwtBuilder loadBuilder() {
//    return Jwts.builder()
//        .setIssuedAt(new Date())
//        .setExpiration(
//            new Date(System.currentTimeMillis() + this.expiration)
//        )
//        .signWith(SignatureAlgorithm.HS512, this.jwtSecret);
//  }
}
