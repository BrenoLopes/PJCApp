package br.balladesh.pjcappbackend.controllers.security.signup;

import br.balladesh.pjcappbackend.dto.MessageResponse;
import br.balladesh.pjcappbackend.dto.security.UserSignUpRequest;
import br.balladesh.pjcappbackend.entity.UserEntity;
import br.balladesh.pjcappbackend.repository.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {
  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  void testWithValidData() {
    UserSignUpRequest request = new UserSignUpRequest("ohno@ohno.com", "ohno", "123456");

    Mockito.when(this.userRepository.existsByEmail("ohno@ohno.com"))
        .thenReturn(false);
    Mockito.when(this.passwordEncoder.encode("123456"))
        .thenReturn("SomeKindaBcryptedData");

    UserEntity user = new UserEntity(
        "ohno",
        "ohno@ohno.com",
        "SomeKindaBcryptedData"
    );
    Mockito.when(this.userRepository.save(user)).thenReturn(user);

    SignUpController testTarget = new SignUpController(this.userRepository, this.passwordEncoder);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(request);

    assertSame(HttpStatus.OK, response.getStatusCode());
    assertEquals("The request was successful.", response.getBody().getMessage());
  }

  @Test
  void testWithoutData() {
    SignUpController testTarget = new SignUpController(null, null);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(null);

    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotEquals("", response.getBody().getMessage());
  }

  @Test
  void testWithoutValidEmail() {
    UserSignUpRequest request = new UserSignUpRequest("ohnoohno.com", "ohno", "123456");

    SignUpController testTarget = new SignUpController(this.userRepository, this.passwordEncoder);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(request);

    assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotEquals("", response.getBody().getMessage());
  }

  @Test
  void testConflict() {
    UserSignUpRequest request = new UserSignUpRequest("ohno@ohno.com", "ohno", "123456");

    Mockito.when(this.userRepository.existsByEmail("ohno@ohno.com"))
        .thenReturn(true);

    SignUpController testTarget = new SignUpController(this.userRepository, this.passwordEncoder);
    ResponseEntity<MessageResponse> response = testTarget.registerUser(request);

    assertSame(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotEquals("", response.getBody().getMessage());
  }
}