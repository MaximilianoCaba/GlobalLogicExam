package microservice.user.serviceTest;

import microservice.user.exeption.SignUpException;
import microservice.user.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SecurityServiceTest {

  @Autowired
  private SecurityService securityService;

  @Test
  void whenGenerateToken_whitValidString_thenReturnValidToken() {
    String token = securityService.generateToken("testUser@example.com");
    assertNotNull(token);
    assertFalse(token.isEmpty());
  }

  @Test
  void whenValidateJwtTokenAndGetEmail_whitInvalidValidToken_thenSignUpException() {
    String token = securityService.generateToken("testUser@example.com");
    String email = securityService.validateJwtTokenAndGetEmail(token);

    assertEquals(email, "testUser@example.com");
  }

  @Test
  void whenValidateJwtTokenAndGetEmail_whitValidToken_thenReturnValidToken() {
    Exception exception = assertThrows(SignUpException.class, () -> securityService.validateJwtTokenAndGetEmail("invalid token"));

    assertEquals("invalid token", exception.getMessage());
  }

}
