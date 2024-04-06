package microservice.user.exeption;

public class SignUpException extends RuntimeException {

  public SignUpException(String message) {
    super(message);
  }
}
