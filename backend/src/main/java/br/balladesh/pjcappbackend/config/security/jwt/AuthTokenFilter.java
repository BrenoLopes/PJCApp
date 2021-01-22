package br.balladesh.pjcappbackend.config.security.jwt;

import br.balladesh.pjcappbackend.config.security.services.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * This class intercept the request before others to setup the security context. So,
 * don't forget to keep chaining the request to the framework.
 *
 */
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired private JwtUtilities jwtUtils;

  @Autowired private MyUserDetailsService userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  public AuthTokenFilter() {}

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException
  {
    try {
      // Check if the jwt token is present in the header. So if the user is trying to access
      // a protected resource, the app will use it to authenticate. If it's valid, the app will
      // setup the framework Authentication object with the user. If not, it'll receive a forbidden
      // http status later on.
      Optional<String> jwt = this.parseJwtFromRequest(request);
      JwtStatus jwtStatus = jwt.isPresent()
          ? this.jwtUtils.checkJwtStatus(jwt.get())
          : JwtStatus.NOT_PRESENT;

      switch (jwtStatus) {
        case VALID:
          Optional<String> username = jwtUtils.extractUsername(jwt.get());

          if (!username.isPresent())
            throw new Exception("The jwt token is invalid, and could not be processed!");

          this.setUpJwtAndAuthenticateUser(request, username.get());
          request.setAttribute("jwt_is_valid", true);
          request.setAttribute("jwt_token", jwt.get());
          request.setAttribute("username", username.get());
          break;
        case EXPIRED:
          Optional<ExpiredJwtException> e = this.jwtUtils.getExceptionFromExpiredToken(jwt.get());
          e.ifPresent(exception ->
              this.refreshTheExpiredTokenIfUrlIsRight(request, exception.getClaims())
          );
          break;
        case CORRUPTED:
        default:
          // Ignore if the jwt token is corrupted and treat it as being without it
      }
    } catch(Exception e) {
      logger.error("Cannot authenticate: {}", e.toString());
    }

    filterChain.doFilter(request, response);
  }

  private void setUpJwtAndAuthenticateUser(HttpServletRequest request, String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
    );
    auth.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
    );

    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  private void refreshTheExpiredTokenIfUrlIsRight(HttpServletRequest request, Claims claims) {
    final String requestUrl = request.getRequestURL().toString();

    // Don't process request from any other url
    if (!requestUrl.contains("auth/refresh")) { return; }

    // Create an empty user and set it into the authentication context so later the app
    // can take the chance to refresh the token in the controller
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        null, null, null
    );

    SecurityContextHolder.getContext().setAuthentication(authToken);
    request.setAttribute("claims", claims);
  }

  private Optional<String> parseJwtFromRequest(HttpServletRequest request) {
    return this.jwtUtils.extractTokenFromHeader(request.getHeader("Authorization"));
  }
}
