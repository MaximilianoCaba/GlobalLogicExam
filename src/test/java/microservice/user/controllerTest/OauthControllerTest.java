package microservice.user.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.user.controller.OauthController;
import microservice.user.dto.SignUpDTO;
import microservice.user.dto.UserDTO;
import microservice.user.exeption.SignUpException;
import microservice.user.service.SecurityService;
import microservice.user.service.UserService;
import microservice.user.validator.SignUpDTOValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = OauthController.class)
public class OauthControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private Authentication authentication;

  @MockBean
  private SecurityService securityService;

  @MockBean
  private UserService userService;

  @TestConfiguration
  static class SignUpDTOValidatorTestContextConfiguration {
    @Bean
    public SignUpDTOValidator signUpDTOValidator() {
      return new SignUpDTOValidator();
    }
  }

  private final String token = "Bearer token";
  private final String email = "myEmail@email.com";

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void whenUserCallLogin_whitValidTokenAndDontHaveSession_thenReturnUserDto() throws Exception {
    UserDTO user = new UserDTO();
    user.setName("test username");

    when(authentication.getPrincipal()).thenReturn(user);

    when(securityService.validateJwtTokenAndGetEmail("token")).thenReturn(email);
    when(userService.getUserByEmail(email)).thenReturn(new UserDTO());

    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication())
        .thenReturn(null)
        .thenReturn(auth);
    SecurityContextHolder.setContext(securityContext);

    mvc.perform(post("/login")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenUserCallLogin_whitValidTokenAndHHaveSession_thenReturnUserDto() throws Exception {
    UserDTO user = new UserDTO();
    user.setName("test username");

    when(authentication.getPrincipal()).thenReturn(user);

    when(securityService.validateJwtTokenAndGetEmail("token")).thenReturn(email);
    when(userService.getUserByEmail(email)).thenReturn(new UserDTO());

    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
    SecurityContextHolder.setContext(securityContext);

    mvc.perform(post("/login")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenUserCallLogin_whitInvalidToken_thenReturnUnauthorized() throws Exception {
    UserDTO user = new UserDTO();
    user.setName("test username");

    when(authentication.getPrincipal()).thenReturn(user);

    when(securityService.validateJwtTokenAndGetEmail("token"))
        .thenThrow(new SignUpException("invalid email"));

    mvc.perform(post("/login")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void testSignUp_WithPasswordTooShor_thenReturnInvalidRequest() throws Exception {
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setPassword("abc");
    signUpDTO.setEmail("myEmail@email.com");

    ObjectMapper objectMapper = new ObjectMapper();
    String signUpDTOJson = objectMapper.writeValueAsString(signUpDTO);

    mvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signUpDTOJson))
            .andExpect(status().isBadRequest());

    Mockito.verify(userService, never()).signUp(any(SignUpDTO.class));
  }

  @Test
  public void testSignUp_WithPasswordDontHave2Numbers_thenReturnInvalidRequest() throws Exception {
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setPassword("Mypas22s2");
    signUpDTO.setEmail("myEmail@email.com");

    ObjectMapper objectMapper = new ObjectMapper();
    String signUpDTOJson = objectMapper.writeValueAsString(signUpDTO);

    mvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signUpDTOJson))
        .andExpect(status().isBadRequest());

    Mockito.verify(userService, never()).signUp(any(SignUpDTO.class));
  }


  @Test
  public void testSignUp_WithPasswordDontHave1CapitalLetter_thenReturnInvalidRequest() throws Exception {
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setPassword("mypas22s");
    signUpDTO.setEmail("myEmail@email.com");

    ObjectMapper objectMapper = new ObjectMapper();
    String signUpDTOJson = objectMapper.writeValueAsString(signUpDTO);

    mvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signUpDTOJson))
        .andExpect(status().isBadRequest());

    Mockito.verify(userService, never()).signUp(any(SignUpDTO.class));
  }

  @Test
  public void testSignUp_WithEmailIsInvalid_thenReturnInvalidRequest() throws Exception {
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setPassword("Mypass12");
    signUpDTO.setEmail("myEmailemail.com");

    ObjectMapper objectMapper = new ObjectMapper();
    String signUpDTOJson = objectMapper.writeValueAsString(signUpDTO);

    mvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signUpDTOJson))
        .andExpect(status().isBadRequest());

    Mockito.verify(userService, never()).signUp(any(SignUpDTO.class));
  }

  @Test
  public void testSignUp_WithEmailIsEmpty_thenReturnInvalidRequest() throws Exception {
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setPassword("Mypass12");

    ObjectMapper objectMapper = new ObjectMapper();
    String signUpDTOJson = objectMapper.writeValueAsString(signUpDTO);

    mvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signUpDTOJson))
        .andExpect(status().isBadRequest());

    Mockito.verify(userService, never()).signUp(any(SignUpDTO.class));
  }

  @Test
  public void testSignUp_WithEmailAndPasswordAreValid_thenReturnUserRegistered() throws Exception {
    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setPassword("Mypass12");
    signUpDTO.setEmail("myEmail@email.com");

    when(userService.signUp(signUpDTO)).thenReturn(new UserDTO());

    ObjectMapper objectMapper = new ObjectMapper();
    String signUpDTOJson = objectMapper.writeValueAsString(signUpDTO);

    MvcResult response = mvc.perform(post("/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signUpDTOJson))
        .andExpect(status().isCreated()).andReturn();

    assertNotNull(response.getResponse().getContentAsString());
  }
}
