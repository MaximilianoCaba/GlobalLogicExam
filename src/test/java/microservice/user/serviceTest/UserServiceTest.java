package microservice.user.serviceTest;

import microservice.user.dto.PhoneDTO;
import microservice.user.dto.SignUpDTO;
import microservice.user.dto.UserDTO;
import microservice.user.entity.PhoneEntity;
import microservice.user.entity.UserEntity;
import microservice.user.exeption.SignUpException;
import microservice.user.exeption.UserNotFoundException;
import microservice.user.repository.UserRepository;
import microservice.user.service.SecurityService;
import microservice.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class UserServiceTest {

  @MockBean
  private SecurityService securityService;

  @Autowired
  private UserServiceImpl userService;

  @Autowired
  private UserRepository userRepository;
  private static boolean userRepositoryInitialized = false;
  private static final String email = "email@example.com";

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);

    if (!userRepositoryInitialized) {
      PhoneEntity phoneEntity = new PhoneEntity();
      phoneEntity.setCityCode(12);
      phoneEntity.setCountryCode("AR");
      phoneEntity.setNumber(123L);

      UserEntity userEntity = new UserEntity();
      userEntity.setEmail(email);
      userEntity.setName("myName");
      userEntity.setPassword("password");
      userEntity.getPhones().add(phoneEntity);

      userRepository.save(userEntity);
      userRepositoryInitialized = true;
    }
  }

  @Test
  void whenSignUp_whitValidSignUpDTO_thenSaveUserInDBAndReturnUserDto() {
    long beforeCount = userRepository.count();

    String email = "email2@example.com";
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setEmail(email);
    signUpDTO.setName("myName");
    signUpDTO.setPassword("password");

    PhoneDTO phoneDTO = new PhoneDTO();
    phoneDTO.setCityCode(12);
    phoneDTO.setCountryCode("AR");
    phoneDTO.setNumber(123);
    signUpDTO.getPhones().add(phoneDTO);

    Mockito.when(securityService.generateToken(anyString())).thenReturn("token");

    UserDTO userDTO = userService.signUp(signUpDTO);

    long afterCount = userRepository.count();

    assertEquals(beforeCount + 1, afterCount);
    assertNotNull(userDTO);
    assertEquals(signUpDTO.getEmail(), userDTO.getEmail());
    assertEquals("token", userDTO.getToken());
    assertEquals("myName", userDTO.getName());
  }

  @Test
  void whenSignUp_whitEmailAlreadyExist_thenThrowSignUpException() {
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setEmail(email);
    signUpDTO.setPassword("pass");

    SignUpException exception = assertThrows(SignUpException.class, () -> userService.signUp(signUpDTO));

    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains("User already exists"));
  }

  @Test
  void whenGetUserByEmail_whitEmailAlreadyExist_thenReturnEmail() {
    UserDTO userDTO = userService.getUserByEmail(email);

    assertEquals(email, userDTO.getEmail());
  }

  @Test
  void whenGetUserByEmail_whitEmailDontExist_thenThrowSignUpException() {
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> userService.getUserByEmail("invalidEmail@example.com")
    );

    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains("Invalid email"));
  }

}
