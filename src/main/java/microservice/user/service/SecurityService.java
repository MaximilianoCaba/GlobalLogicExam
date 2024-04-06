package microservice.user.service;

import microservice.user.exeption.SignUpException;

public interface SecurityService {
  String generateToken(String string);

  String validateJwtTokenAndGetEmail(String token) throws SignUpException;

}
