package br.balladesh.pjcappbackend.config.security.services;

import br.balladesh.pjcappbackend.entity.security.UserEntity;
import br.balladesh.pjcappbackend.utilities.defaults.Defaults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Simple implementation of these class for easily storing this information inside
 * spring's authentication object.</p>
 *
 * <p>As of right now, this application uses the email parameter as the username and
 * doesn't require the validation of permissions.</p>
 * <br/>
 * <p>If the application so requires, please update this to include more information
 * about the user for easily handling permissions in the controller.</p>
 * <br/>
 * <p>Example of how to create this object:</p>
 * <code>new MyUserDetails(id, username, email, password, authorities)</code>
 */
public class MyUserDetails implements UserDetails {
  private final Long id;
  private final String username;
  private final String email;

  @JsonIgnore
  private final String password;

  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * <p>Note that this app uses the email address as username, so you can set username to null and set
   * the authorities into null or an empty list</p>
   *
   * @param id The user's id
   * @param username The user's email address
   * @param email The user's email address and username
   * @param password The user's password
   * @param authorities The authorities available for this user
   */
  public MyUserDetails(
      Long id,
      String username,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities
  ) {
    this.id = MoreObjects.firstNonNull(id, Defaults.getDefaultLong());
    this.username = MoreObjects.firstNonNull(email, Defaults.DEFAULT_STR);
    this.email = MoreObjects.firstNonNull(email, Defaults.DEFAULT_STR);
    this.password = MoreObjects.firstNonNull(password, Defaults.DEFAULT_STR);
    this.authorities = new ArrayList<>();
  }

  public static MyUserDetails build (UserEntity user) {
//    List<GrantedAuthority> authorities = user.getRoles()
//      .stream()
//      .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//      .collect(Collectors.toList());

    return new MyUserDetails(
        user.getId(),
        user.getEmail(),
        user.getEmail(),
        user.getPassword(),
        new ArrayList<>()
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    MyUserDetails that = (MyUserDetails) o;

    return Objects.equal(this.id, that.id)
        && Objects.equal(this.username, that.username)
        && Objects.equal(this.authorities, that.authorities)
        && Objects.equal(this.email, that.email)
        && Objects.equal(this.password, that.password);
  }
}