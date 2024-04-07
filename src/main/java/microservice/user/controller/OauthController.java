package microservice.user.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import microservice.user.dto.SignUpDTO;
import microservice.user.dto.UserDTO;
import microservice.user.service.UserService;
import microservice.user.validator.SignUpDTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class OauthController {

  private final UserService userService;
  private final SignUpDTOValidator signUpDTOValidator;

  @InitBinder("signUpDTO")
  protected void initBinder(final WebDataBinder binder) {
    binder.addValidators(signUpDTOValidator);
  }

  @Operation(description = "Authenticate user by email", security =  { @SecurityRequirement(name = "bearerAuth") })
  @PostMapping("/login")
  public UserDTO login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (UserDTO) authentication.getPrincipal();
  }

  @Operation(description = "Register a new account by email")
  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
    UserDTO userResponse = userService.signUp(signUpDTO);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }

}
