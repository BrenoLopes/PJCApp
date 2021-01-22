package br.balladesh.pjcappbackend.config.security.services;

import br.balladesh.pjcappbackend.entity.security.UserEntity;
import br.balladesh.pjcappbackend.repository.security.UserRepository;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {
  @Mock
  private UserRepository userRepository;
  private MyUserDetailsService myUserDetailsService;

  @BeforeEach
  void setUpTestTarget() {
    myUserDetailsService = new MyUserDetailsService(this.userRepository);
  }

  @Test
  void loadValidUserFromTheDatabase() {
    UserEntity expected = new UserEntity(
        "ohno",
        "ohno@ohno.com",
        "RandomBCryptedPassword"
    );
    String username = "ohno@ohno.com";
    Mockito.when(this.userRepository.findByEmail(username)).thenReturn(Optional.of(expected));

    UserDetails received = this.myUserDetailsService.loadUserByUsername(username);

    assertEquals(expected.getEmail(), received.getUsername());
    assertEquals(expected.getPassword(), received.getPassword());
    assertEquals(Lists.newArrayList(), received.getAuthorities());
    assertTrue(received.isEnabled());
  }

  @Test
  void loadNonExistentUserFromTheDatabase() {
    String username = "ohno@ohno.com";
    Mockito.when(this.userRepository.findByEmail(username)).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> this.myUserDetailsService.loadUserByUsername(username));
  }
}