package microservice.user.service;

import microservice.user.dto.SignUpDTO;
import microservice.user.dto.UserDTO;
import microservice.user.exeption.SignUpException;

public interface UserService {

  UserDTO signUp(SignUpDTO signUpRequest);

  UserDTO getUserByEmail(String email) throws SignUpException;
}
